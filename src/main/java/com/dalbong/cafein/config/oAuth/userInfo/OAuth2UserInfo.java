package com.dalbong.cafein.config.oAuth.userInfo;

import com.dalbong.cafein.domain.member.Member;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public abstract String getId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract String getImageUrl();

    public abstract LocalDate getBirth();

    public abstract Optional<Member> getMember();

}

