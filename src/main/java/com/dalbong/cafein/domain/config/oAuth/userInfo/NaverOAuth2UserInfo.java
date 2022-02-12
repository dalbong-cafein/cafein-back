package com.dalbong.cafein.domain.config.oAuth.userInfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo{

    public NaverOAuth2UserInfo(Map<String, Object> attributes){
        super(attributes);
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public String getImageUrl() {
        return null;
    }
}
