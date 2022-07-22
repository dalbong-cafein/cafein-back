package com.dalbong.cafein.oAuth;

import com.dalbong.cafein.domain.member.MemberState;
import com.dalbong.cafein.oAuth.userInfo.OAuthUserInfo;
import com.dalbong.cafein.oAuth.userInfo.OAuthUserInfoFactory;
import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.MemberImageRepository;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.dto.login.AccountUniteResDto;
import com.dalbong.cafein.handler.exception.AlreadyExistedAccountException;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;

import static com.dalbong.cafein.domain.member.AuthProvider.KAKAO;
import static com.dalbong.cafein.domain.member.AuthProvider.NAVER;

@RequiredArgsConstructor
@Transactional
@Service
public class SocialLoginService {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 소셜 로그인
     */
    @Transactional
    public Member login(AuthProvider authProvider, String oAuthAccessToken) throws JsonProcessingException {

        OAuthUserInfo userInfo = getOAuthUserInfo(authProvider, oAuthAccessToken);

        //DB : Member 조회 - 탈퇴 회원 제외
        Optional<Member> memberResult = userInfo.getMember();

        //신규 회원
        if(memberResult.isEmpty()){

            //기존 회원 중 같은 email을 사용하고 있는 회원이 있는 지 체크
           // Optional<Member> emailDuplicateResult = memberRepository.findByEmail(userInfo.getEmail());

            //해당 email을 사용 중인 계정이 없는 경우
           // if (emailDuplicateResult.isEmpty()){
                return signUp(authProvider, userInfo);
           // }
            //해당 email을 사용 중인 계정이 있는 경우
           // else{
            //    AccountUniteResDto accountUniteResDto = new AccountUniteResDto(
           //             userInfo.getEmail(), emailDuplicateResult.get().getRegDateTime(), userInfo.getId(), authProvider);

                //기억!! 현재는
            //    throw new AlreadyExistedAccountException("이미 해당 email을 사용중인 계정이 존재합니다.", accountUniteResDto);
           // }
        }
        // 기존 회원
        else{
            Member findMember = memberResult.get();

            if (findMember.getMainAuthProvider().equals(authProvider)){

                //사용자정보에 변경이 있다면 사용자 정보를 업데이트 해준다.
                updateMember(findMember, userInfo);
            }

            return findMember;
        }
    }

    private void updateMember(Member member, OAuthUserInfo userInfo) {

        //이름 변경
        if (userInfo.getName() != null && !member.getUsername().equals(userInfo.getName())) {
            member.changeUsername(userInfo.getName());
        }


        //생년월일 변경
        if(userInfo.getBirth() != null && !member.getBirth().equals(userInfo.getBirth())){
            member.changeBirth(userInfo.getBirth());
        }

        //프로필 이미지 변경
//        if (userInfo.getImageUrl() != null && !member.getImageUrl().equals(userInfo.getImageUrl())) {
//            member.changeImageUrl(userInfo.getImageUrl());
//        }
    }

    private Member signUp(AuthProvider authProvider, OAuthUserInfo userInfo) {
        Member member;
        MemberImage memberImage;

        //랜덤값 비밀번호 생성
        String password = passwordEncoder.encode(UUID.randomUUID().toString());

        switch (authProvider) {
            case KAKAO:
                member = Member.builder()
                        .kakaoId(userInfo.getId())
                        .password(password)
                        .username(userInfo.getName())
                        .birth(userInfo.getBirth())
                        .mainAuthProvider(KAKAO)
                        .state(MemberState.NORMAL)
                        .build();

                if(userInfo.getEmail() != null){
                    member.changeEmail(userInfo.getEmail());
                }

                //성별 데이터
                if(userInfo.getGender().isPresent()){
                    member.changeGender(userInfo.getGender().get());
                }

                memberRepository.save(member);
                break;

            case NAVER:
                member = Member.builder()
                        .naverId(userInfo.getId())
                        .password(password)
                        .username(userInfo.getName())
                        .email(userInfo.getEmail())
                        .birth(userInfo.getBirth())
                        .mainAuthProvider(NAVER)
                        .state(MemberState.NORMAL)
                        .build();

                //성별 데이터
                if(userInfo.getGender().isPresent()){
                    member.changeGender(userInfo.getGender().get());
                }

                memberRepository.save(member);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + authProvider);
        }

        //프로필 사진 저장
        memberImage = new MemberImage(member, userInfo.getImageUrl(), true);

        memberImageRepository.save(memberImage);

        return member;
    }


    /**
     * OAuthUserInfo 조회
     */
    private OAuthUserInfo getOAuthUserInfo(AuthProvider authProvider, String oAuthAccessToken) throws JsonProcessingException {

        switch (authProvider){
            case KAKAO:
                return getKakaoUserInfo(oAuthAccessToken);

            case NAVER:
                return getNaverUserInfo(oAuthAccessToken);

            case APPLE:
                return getAppleUserInfo(oAuthAccessToken);

            default:
                throw new CustomException(authProvider + "은 지원하지 않는 소셜 제공자입니다.");
        }
    }



    private OAuthUserInfo getNaverUserInfo(String oAuthAccessToken) {

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + oAuthAccessToken);

        Map<String,Object> attributes = restTemplate.postForObject(
                "https://openapi.naver.com/v1/nid/me", new HttpEntity<>(header), Map.class);

        return OAuthUserInfoFactory.getOAuth2UserInfo(AuthProvider.NAVER, attributes, memberRepository);
    }

    private OAuthUserInfo getKakaoUserInfo(String oAuthAccessToken){

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + oAuthAccessToken);

        Map<String,Object> attributes = restTemplate.postForObject(
                "https://kapi.kakao.com/v2/user/me", new HttpEntity<>(headers), Map.class);

        return OAuthUserInfoFactory.getOAuth2UserInfo(AuthProvider.KAKAO, attributes, memberRepository);
    }

    private OAuthUserInfo getAppleUserInfo(String oAuthAccessToken) throws JsonProcessingException {

        //Http요청하기 - GET 방식으로 -그리고 response 변수의 응답 받음.
        Keys keys = restTemplate.getForObject("https://appleid.apple.com/auth/keys", Keys.class);

        String[] decodeArray = oAuthAccessToken.split("\\.");


        String header = new String(Base64.getDecoder().decode(decodeArray[0]));
        String payload = new String(Base64.getDecoder().decode(decodeArray[1]));
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String,String> headerMap = objectMapper.readValue(header, Map.class);
        Map<String,Object> map = objectMapper.readValue(payload, Map.class);
        String email = (String) map.get("email");




        System.out.println(Arrays.toString(decodeArray));
        System.out.println("------------------");
        System.out.println(header);
        System.out.println(payload);
        System.out.println(email);

        Key findKey = null;
        if(keys.getKeys() != null){
            for (Key key : keys.getKeys()){
                String kid = headerMap.get("kid");
                if(kid.equals(key.getKid())){
                    findKey = key;
                }
            }
            System.out.println(findKey);
        }

        PublicKey publicKey = getPublicKey(findKey);
        Claims claims = Jwts.parser().setSigningKey(publicKey).parseClaimsJws(oAuthAccessToken).getBody();

        System.out.println(claims.toString());


        return null;
    }

    public PublicKey getPublicKey(Key key) {

        String nStr = key.getN();
        String eStr = key.getE();
        System.out.println(nStr);
        System.out.println(eStr);

        byte[] nBytes = Base64.getUrlDecoder().decode(nStr);
        byte[] eBytes = Base64.getUrlDecoder().decode(eStr);

        BigInteger n = new BigInteger(1, nBytes);
        BigInteger e = new BigInteger(1, eBytes);

        try{

            RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(n, e);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePublic(rsaPublicKeySpec);


        }catch (Exception exception) {

        }

        return null;
    }
}
