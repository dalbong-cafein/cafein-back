package com.dalbong.cafein.oAuth.userInfo;

import com.dalbong.cafein.domain.member.GenderType;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

public class AppleOAuthUserInfo extends OAuthUserInfo{

    private final Map<String,Object> attributes;
    private final MemberRepository memberRepository;

    public AppleOAuthUserInfo(Map<String,Object> attributes, MemberRepository memberRepository){
        super(attributes);
        this.attributes = attributes;
        this.memberRepository = memberRepository;
    }

    @Override
    public String getId() {

        return attributes.get("appleId").toString();
    }

    @Override
    public String getName() {
        if(attributes.get("username") != null){
            return attributes.get("username").toString();
        }
        return null;
    }

    @Override
    public String getEmail() {
        return attributes.get("email").toString();
    }

    @Override
    public String getImageUrl() {
        return null;
    }

    @Override
    public Optional<LocalDate> getBirth() {

        return Optional.empty();
    }

    @Override
    public Optional<GenderType> getGender() {
        return Optional.empty();
    }

    @Override
    public Optional<Member> getMember() {
        return memberRepository.findByAppleIdAndNotLeave(getId());
    }
}
