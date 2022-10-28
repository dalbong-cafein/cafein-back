package com.dalbong.cafein.oAuth.apple;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.oAuth.userInfo.OAuthUserInfoFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AppleTokenService {

    @Value("${apple.keyId}")
    private String keyId;

    @Value("${apple.clientId}")
    private String clientId;

    @Value("${apple.teamId}")
    private String teamId;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public Map<String, Object> verify(String authToken, Keys keys) throws JsonProcessingException {

        //token 분리
        String[] decodeArray = authToken.split("\\.");

        //header 얻기
        String header = new String(Base64.getDecoder().decode(decodeArray[0]));

        Map<String,String> headerMap = objectMapper.readValue(header, Map.class);

        String kid = headerMap.get("kid");

        //authToken 의 public key 찾기
        Key findKey = findApplePublicKey(kid, keys);

        //새로운 public key 생성
        PublicKey publicKey = getPublicKey(findKey);

        //token 검증
        try {
            Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(authToken).getBody();

            String appleId = claims.get("sub", String.class);
            String email = claims.get("email", String.class);

            Map<String,Object> attributes = new HashMap<>();
            attributes.put("appleId", appleId);
            attributes.put("email", email);

            return attributes;

        }catch (ExpiredJwtException e){
            throw new CustomException("유효기간이 만료된 토큰입니다.");
        }
    }

    private Key findApplePublicKey(String kid, Keys keys) {

        //authToken 의 public key 찾기
        if(keys.getKeys() != null){
            for (Key key : keys.getKeys()){
                if(kid.equals(key.getKid())){
                    return key;
                }
            }
        }

        throw new CustomException("공개키를 찾지 못했습니다.");
    }

    private PublicKey getPublicKey(Key key) {

        String nStr = key.getN();
        String eStr = key.getE();

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr);
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr);

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try{
            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(rsaPublicKeySpec);


        }catch (Exception exception) {
            throw new CustomException("공개키를 얻지 못했습니다.");
        }
    }

    public void revoke(String authorizationCode) throws IOException {

        String clientSecret = createClientSecret();

        AppleToken appleToken = generateAuthToken(clientSecret, authorizationCode);

        RestTemplate restTemplate = new RestTemplateBuilder().build();
        String revokeUrl = "https://appleid.apple.com/auth/revoke";

        LinkedMultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("token", appleToken.getRefresh_token());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(revokeUrl, httpEntity, String.class);

        if(stringResponseEntity.getStatusCode().is4xxClientError()){
            throw new CustomException("회원 탈퇴 실패");
        }
    }


    private AppleToken generateAuthToken(String clientSecret, String authorizationCode) throws IOException {

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        try{
            ResponseEntity<AppleToken> responseEntity = restTemplate.postForEntity("https://appleid.apple.com/auth/token", new HttpEntity<>(params, headers), AppleToken.class);
            return responseEntity.getBody();
        }catch (HttpClientErrorException e) {
            throw new CustomException("Apple Auth Token Error");
        }
    }

    private String createClientSecret() throws IOException {
        Date expirationDate = Date.from(LocalDateTime.now().plusDays(30).atZone(ZoneId.systemDefault()).toInstant());
        Map<String, Object> jwtHeader = new HashMap<>();
        jwtHeader.put("kid", keyId);
        jwtHeader.put("alg", "ES256");

        return Jwts.builder()
                .setHeaderParams(jwtHeader)
                .setIssuer(teamId)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 발행 시간 - UNIX 시간
                .setExpiration(expirationDate) // 만료 시간
                .setAudience("https://appleid.apple.com")
                .setSubject(clientId)
                .signWith(SignatureAlgorithm.ES256, getPrivateKey())
                .compact();
    }

    private PrivateKey getPrivateKey() throws IOException {
        ClassPathResource resource = new ClassPathResource("C:/Users/hyeongwoo/intelliJ-workspace.AuthKey.p8");
        String privateKey = new String(Files.readAllBytes(Paths.get(resource.getURI())));

        Reader pemReader = new StringReader(privateKey);
        PEMParser pemParser = new PEMParser(pemReader);
        JcaPEMKeyConverter converter = new JcaPEMKeyConverter();
        PrivateKeyInfo object = (PrivateKeyInfo) pemParser.readObject();
        return converter.getPrivateKey(object);
    }
}
