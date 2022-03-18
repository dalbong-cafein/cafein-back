package com.dalbong.cafein.config.oAuth;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.config.oAuth.userInfo.OAuth2UserInfo;
import com.dalbong.cafein.config.oAuth.userInfo.OAuth2UserInfoFactory;
import com.dalbong.cafein.domain.image.MemberImage;
import com.dalbong.cafein.domain.image.MemberImageRepository;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.dto.login.AccountUniteResDto;
import com.dalbong.cafein.handler.exception.AlreadyExistedAccountException;
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

        OAuth2UserInfo userInfo = OAuth2UserInfoFactory.getOAuth2UserInfo(authProvider, attributes, memberRepository);

        //DB : Member 조회
        Optional<Member> memberResult = userInfo.getMember();

        //신규 회원
        if(memberResult.isEmpty()){

            //기존 회원 중 같은 email을 사용하고 있는 회원이 있는 지 체크
            Optional<Member> emailDuplicateResult = memberRepository.findByEmail(userInfo.getEmail());

            //해당 email을 사용 중인 계정이 없는 경우
            if (emailDuplicateResult.isEmpty()){
                Member member;
                MemberImage memberImage;

                //랜덤값 비밀번호 생성
                BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
                String password = passwordEncoder.encode(UUID.randomUUID().toString());

                switch (authProvider) {
                    case KAKAO:
                        member = Member.builder()
                                .kakaoId(userInfo.getId())
                                .password(password)
                                .username(userInfo.getName())
                                .birth(userInfo.getBirth())
                                .mainAuthProvider(KAKAO)
                                .build();

                        if(userInfo.getEmail() != null){
                            member.changeEmail(userInfo.getEmail());
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
            //해당 email을 사용 중인 계정이 있는 경우
            else{
                AccountUniteResDto accountUniteResDto = new AccountUniteResDto(
                        userInfo.getEmail(), emailDuplicateResult.get().getRegDate(), userInfo.getId(), authProvider);

                throw new AlreadyExistedAccountException("이미 해당 email을 사용중인 계정이 존재합니다.", accountUniteResDto);
            }
        }
        // 기존 회원
        else{
            Member findMember = memberResult.get();

            if (findMember.getMainAuthProvider().equals(authProvider)){

                //사용자정보에 변경이 있다면 사용자 정보를 업데이트 해준다.
                updateMember(findMember, userInfo);
            }

            return new PrincipalDetails(findMember, userInfo);
        }

        
    }

    private void updateMember(Member member, OAuth2UserInfo userInfo) {

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
}
