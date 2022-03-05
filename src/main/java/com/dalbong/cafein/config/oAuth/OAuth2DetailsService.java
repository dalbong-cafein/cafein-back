package com.dalbong.cafein.config.oAuth;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.config.oAuth.userInfo.OAuth2UserInfo;
import com.dalbong.cafein.config.oAuth.userInfo.OAuth2UserInfoFactory;
import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.MemberImageRepository;
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
    private final MemberImageRepository memberImageRepository;

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
            MemberImage memberImage;

            //랜덤값 비밀번호 생성
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            String password = passwordEncoder.encode(UUID.randomUUID().toString());

            switch (authProvider) {
                case KAKAO:
                    member = Member.builder()
                            .oauthId(userInfo.getId())
                            .password(password)
                            .username(userInfo.getName())
                            .provider(KAKAO)
                            .build();

                    if(userInfo.getEmail() != null){
                        member.changeEmail(userInfo.getEmail());
                    }

                    memberRepository.save(member);
                    break;

                case NAVER:
                    member = Member.builder()
                            .oauthId(userInfo.getId())
                            .password(password)
                            .username(userInfo.getName())
                            .email(userInfo.getEmail())
                            .provider(NAVER)
                            .build();

                    memberRepository.save(member);
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + authProvider);
            }

            //프로필 사진 저장
            memberImage = new MemberImage(member, userInfo.getImageUrl(), true);
            memberImageRepository.save(memberImage);

            return new PrincipalDetails(member, userInfo);

        }
        // 기존 회원
        else{
            Member findMember = result.get();

            //소셜 제공자 타입이 다를 경우
            if (authProvider != findMember.getProvider()) {
                throw new IllegalStateException(
                        "Looks like you're signed up with " + authProvider +
                                " account. Please use your " + findMember.getProvider() + " account to login."
                );
            }

            //사용자정보에 변경이 있다면 사용자 정보를 업데이트 해준다.
            updateMember(findMember, userInfo);

            return new PrincipalDetails(findMember, userInfo);
        }

        
    }

    private void updateMember(Member member, OAuth2UserInfo userInfo) {

        //이름 변경
        if (userInfo.getName() != null && !member.getUsername().equals(userInfo.getName())) {
            member.changeUsername(userInfo.getName());
        }

        //이메일 변경
        if (userInfo.getEmail() != null && !member.getEmail().equals(userInfo.getEmail())){
            member.changeEmail(userInfo.getEmail());
        }

        //프로필 이미지 변경
//        if (userInfo.getImageUrl() != null && !member.getImageUrl().equals(userInfo.getImageUrl())) {
//            member.changeImageUrl(userInfo.getImageUrl());
//        }
    }
}
