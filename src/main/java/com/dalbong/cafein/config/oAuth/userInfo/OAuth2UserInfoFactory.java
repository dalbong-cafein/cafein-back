package com.dalbong.cafein.config.oAuth.userInfo;


import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider provider,
                                                   Map<String, Object> attributes, MemberRepository memberRepository) {
        switch (provider) {
            case KAKAO: return new KakaoOAuth2UserInfo(attributes, memberRepository);
            case NAVER: return new NaverOAuth2UserInfo(attributes, memberRepository);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
