package com.dalbong.cafein.config.oAuth.userInfo;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;


public class KakaoOAuth2UserInfo extends OAuth2UserInfo{

    protected Map<String,Object> kakaoAccount;
    protected Map<String,Object> profile;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes){
        super(attributes);
        kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        profile = (Map<String, Object>) kakaoAccount.get("profile");
    }

    @Override
    public String getId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getName() {
        return (String) profile.get("nickname");
    }

    @Override
    public String getEmail() {
        return (String) kakaoAccount.get("email");
    }

    @Override
    public String getImageUrl() {
        return (String) profile.get("thumbnail_image_url");
    }

    @Override
    public LocalDate getBirth() {
        String birthday = (String) kakaoAccount.get("birthday");

        //TODO 출생연도 데이터 권한 받을 시 수정 필요

        return LocalDate.parse("9999"+birthday, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
