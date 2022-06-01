package com.dalbong.cafein.oAuth.userInfo;

import com.dalbong.cafein.domain.member.GenderType;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

public class NaverOAuthUserInfo extends OAuthUserInfo {

    private final MemberRepository memberRepository;
    private final Map<String, Object> response;

    public NaverOAuthUserInfo(Map<String, Object> attributes, MemberRepository memberRepository){
        super(attributes);
        response = (Map<String, Object>) attributes.get("response");
        this.memberRepository = memberRepository;
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

    @Override
    public LocalDate getBirth() {

        String birthday = (String) response.get("birthday");
        String birthyear = (String) response.get("birthyear");

        return LocalDate.parse(birthyear+"-"+birthday, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    @Override
    public Optional<GenderType> getGender() {

        String result = (String) response.get("gender");

        GenderType gender = null;

        if(result.equals("F")){
            gender = GenderType.FEMALE;
        }else if(result.equals("M")){
            gender = GenderType.MALE;
        }
        return Optional.ofNullable(gender);
    }

    @Override
    public Optional<Member> getMember() {
        return memberRepository.findByNaverIdAndNotDeleted(getId());
    }
}
