package com.dalbong.cafein.oAuth;

import com.dalbong.cafein.domain.member.MemberState;
import com.dalbong.cafein.dto.login.LoginDto;
import com.dalbong.cafein.oAuth.apple.AppleTokenService;
import com.dalbong.cafein.oAuth.apple.Keys;
import com.dalbong.cafein.oAuth.userInfo.OAuthUserInfo;
import com.dalbong.cafein.oAuth.userInfo.OAuthUserInfoFactory;
import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.MemberImageRepository;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.service.member.MemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.dalbong.cafein.domain.member.AuthProvider.*;

@RequiredArgsConstructor
@Transactional
@Service
public class SocialLoginService {

    private final RestTemplate restTemplate;
    private final MemberRepository memberRepository;
    private final MemberImageRepository memberImageRepository;
    private final PasswordEncoder passwordEncoder;
    private final AppleTokenService appleTokenService;
    private final MemberService memberService;

    /**
     * 소셜 로그인
     */
    @Transactional
    public Member login(LoginDto loginDto, String clientIp) throws JsonProcessingException {

        OAuthUserInfo userInfo = getOAuthUserInfo(loginDto);

        //DB : Member 조회 - 탈퇴 회원 제외
        Optional<Member> memberResult = userInfo.getMember();

        //신규 회원
        if(memberResult.isEmpty()){

            //기존 회원 중 같은 email을 사용하고 있는 회원이 있는 지 체크
            // Optional<Member> emailDuplicateResult = memberRepository.findByEmail(userInfo.getEmail());

            //해당 email을 사용 중인 계정이 없는 경우
            // if (emailDuplicateResult.isEmpty()){
            Member member = signUp(loginDto.getAuthProvider(), userInfo);

            //로그인 기록 저장
            memberService.saveLoginHistory(member, loginDto.getAuthProvider(), clientIp);

            return member;
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
            
            memberService.saveLoginHistory(findMember, loginDto.getAuthProvider(), clientIp);

            return findMember;
        }
    }

    /**
     * 소셜 로그인시 프로필 정보 갱신
     */
    private void updateMember(Member member, OAuthUserInfo userInfo) {

        //이름 변경
        if (userInfo.getName() != null && !member.getUsername().equals(userInfo.getName())) {
            member.changeUsername(userInfo.getName());
        }


        //생년월일 변경
        if(userInfo.getBirth().isPresent() && !member.getBirth().equals(userInfo.getBirth().get())){
            member.changeBirth(userInfo.getBirth().get());
        }

        //프로필 이미지 변경
//        if (userInfo.getImageUrl() != null && !member.getImageUrl().equals(userInfo.getImageUrl())) {
//            member.changeImageUrl(userInfo.getImageUrl());
//        }
    }

    /**
     * 소셜 회원가입
     */
    private Member signUp(AuthProvider authProvider, OAuthUserInfo userInfo) {
        Member member;

        //랜덤값 비밀번호 생성
        String password = passwordEncoder.encode(UUID.randomUUID().toString());

        switch (authProvider) {
            case KAKAO:
                member = Member.builder()
                        .kakaoId(userInfo.getId())
                        .password(password)
                        .username(userInfo.getName())
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
                        .mainAuthProvider(NAVER)
                        .state(MemberState.NORMAL)
                        .build();

                //성별 데이터
                if(userInfo.getGender().isPresent()){
                    member.changeGender(userInfo.getGender().get());
                }

                memberRepository.save(member);
                break;

            case APPLE:
                //username 값 체크
                if(userInfo.getName() == null || userInfo.getName().isBlank()){
                    throw new CustomException("애플 계정 회원가입에 실패하였습니다. - username");
                }

                member = Member.builder()
                        .appleId(userInfo.getId())
                        .password(password)
                        .username(userInfo.getName())
                        .email(userInfo.getEmail())
                        .mainAuthProvider(APPLE)
                        .state(MemberState.NORMAL)
                        .build();

                memberRepository.save(member);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + authProvider);
        }

        //프로필 사진 저장
        if(userInfo.getImageUrl() != null){
            MemberImage memberImage = new MemberImage(member, userInfo.getImageUrl(), true);
            memberImageRepository.save(memberImage);
        }

        //생년월일 저장
        if (userInfo.getBirth().isPresent()){
            member.changeBirth(userInfo.getBirth().get());
        }

        return member;
    }


    /**
     * OAuthUserInfo 조회
     */
    private OAuthUserInfo getOAuthUserInfo(LoginDto loginDto) throws JsonProcessingException {

        AuthProvider authProvider = loginDto.getAuthProvider();
        String authToken = loginDto.getAuthToken();

        switch (authProvider){
            case KAKAO:
                return getKakaoUserInfo(authToken);

            case NAVER:
                return getNaverUserInfo(authToken);

            case APPLE:
                return getAppleUserInfo(authToken, loginDto.getUsername());

            default:
                throw new CustomException(authProvider + "은 지원하지 않는 소셜 제공자입니다.");
        }
    }



    private OAuthUserInfo getNaverUserInfo(String authToken) {

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + authToken);

        Map<String,Object> attributes = restTemplate.postForObject(
                "https://openapi.naver.com/v1/nid/me", new HttpEntity<>(header), Map.class);

        return OAuthUserInfoFactory.getOAuth2UserInfo(AuthProvider.NAVER, attributes, memberRepository);
    }

    private OAuthUserInfo getKakaoUserInfo(String authToken){

        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + authToken);

        Map<String,Object> attributes = restTemplate.postForObject(
                "https://kapi.kakao.com/v2/user/me", new HttpEntity<>(headers), Map.class);

        return OAuthUserInfoFactory.getOAuth2UserInfo(AuthProvider.KAKAO, attributes, memberRepository);
    }

    private OAuthUserInfo getAppleUserInfo(String authToken, String username) throws JsonProcessingException {

        //Http요청하기 - GET 방식으로 -그리고 response 변수의 응답 받음.
        Keys keys = restTemplate.getForObject("https://appleid.apple.com/auth/keys", Keys.class);

        Map<String, Object> attributes = appleTokenService.verify(authToken, keys);

        attributes.put("username", username);

        return OAuthUserInfoFactory.getOAuth2UserInfo(APPLE, attributes, memberRepository);
    }
}
