package com.dalbong.cafein.oAuth.apple;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.oAuth.userInfo.OAuthUserInfoFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.RSAPublicKeySpec;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AppleTokenService {

    private final ObjectMapper objectMapper;

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

    public PublicKey getPublicKey(Key key) {

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
}
