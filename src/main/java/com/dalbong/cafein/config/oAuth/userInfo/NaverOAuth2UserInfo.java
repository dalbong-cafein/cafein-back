package com.dalbong.cafein.config.oAuth.userInfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo{

    protected Map<String, Object> response;

    public NaverOAuth2UserInfo(Map<String, Object> attributes){
        super(attributes);
        response = (Map<String, Object>) attributes.get("response");
    }

    @Override
    public String getId() {
        return (String) response.get("id");
    }

    @Override
    public String getName() {
        return (String) response.get("name");
    }

    @Override
    public String getEmail() {
        return (String) response.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) response.get("profile_image");
    }
}
