package com.dalbong.cafein.oAuth.userInfo;


import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.MemberRepository;

import java.util.Map;

public class OAuthUserInfoFactory {

    public static OAuthUserInfo getOAuth2UserInfo(AuthProvider provider,
                                                  Map<String, Object> attributes, MemberRepository memberRepository) {
        switch (provider) {
            case KAKAO: return new KakaoOAuthUserInfo(attributes, memberRepository);
            case NAVER: return new NaverOAuthUserInfo(attributes, memberRepository);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
