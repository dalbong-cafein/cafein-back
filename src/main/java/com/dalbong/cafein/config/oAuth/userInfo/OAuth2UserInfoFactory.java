package com.dalbong.cafein.config.oAuth.userInfo;


import com.dalbong.cafein.domain.member.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(AuthProvider provider,
                                                   Map<String, Object> attributes) {
        switch (provider) {
            case KAKAO: return new KakaoOAuth2UserInfo(attributes);
            case NAVER: return new NaverOAuth2UserInfo(attributes);
            default: throw new IllegalArgumentException("Invalid Provider Type.");
        }
    }
}
