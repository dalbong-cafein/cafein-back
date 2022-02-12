package com.dalbong.cafein.domain.config.oAuth;

import com.dalbong.cafein.domain.config.auth.PrincipalDetails;
import com.dalbong.cafein.domain.config.oAuth.userInfo.OAuth2UserInfo;
import com.dalbong.cafein.domain.config.oAuth.userInfo.OAuth2UserInfoFactory;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.dalbong.cafein.domain.member.AuthProvider.KAKAO;
import static com.dalbong.cafein.domain.member.AuthProvider.NAVER;


@RequiredArgsConstructor
@Service
public class OAuth2DetailsService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        // oauth 제공해주는 사이트마다 제공해주는 데이터 변수명이 다름
        Map<String, Object> attributes = oAuth2User.getAttributes();

        System.out.println(attributes);

        //로그인한 사이트
        AuthProvider authProvider =
                AuthProvider.valueOf(
                        userRequest.getClientRegistration().getRegistrationId().toUpperCase());

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, attributes);

        //신규,기존 회원 체크
        Optional<Member> result = memberRepository.findByOauthId(userInfo.getId());

        //신규 회원
        if(result.isEmpty()){
            Member member;

            //랜덤값 비밀번호 생성
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password = passwordEncoder.encode(UUID.randomUUID().toString());

            //TODO 1. image 필수,선택  2. email 추가? 3. 데이터 수정 시 로직 추가
            switch (authProvider) {
                case KAKAO:
                    member = Member.builder()
                            .oauthId(userInfo.getId())
                            .password(password)
                            .username(userInfo.getName())
                            .provider(KAKAO)
                            .build();

                    if(userInfo.getImageUrl() != null){
                        member.changeImageUrl(userInfo.getImageUrl());
                    }

                    memberRepository.save(member);
                    break;

                case NAVER:
                    member = Member.builder()
                            .oauthId(userInfo.getId())
                            .password(password)
                            .username(userInfo.getName())
                            .provider(NAVER)
                            .imageUrl(userInfo.getImageUrl())
                            .build();

                    memberRepository.save(member);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + authProvider);
            }

            return new PrincipalDetails(member, userInfo);

        }
        // 기존 회원
        else{
            return new PrincipalDetails(result.get(), userInfo);
        }
    }
}
