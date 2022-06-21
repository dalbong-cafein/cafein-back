package com.dalbong.cafein.oAuth.userInfo;

import com.dalbong.cafein.domain.member.GenderType;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public class KakaoOAuthUserInfo extends OAuthUserInfo {

    private final Map<String,Object> kakaoAccount;
    private final Map<String,Object> profile;
    private final MemberRepository memberRepository;

    public KakaoOAuthUserInfo(Map<String, Object> attributes, MemberRepository memberRepository){
        super(attributes);
        kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        profile = (Map<String, Object>) kakaoAccount.get("profile");
        this.memberRepository = memberRepository;
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
        if (birthday == null){
            birthday = "0231";
        }
        return LocalDate.parse("9999"+birthday, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }

    @Override
    public Optional<GenderType> getGender() {

        boolean hasGender = (boolean) kakaoAccount.get("has_gender");
        String result = (String) kakaoAccount.get("gender");

        GenderType gender = null;
        if(hasGender && result != null){
            gender = GenderType.valueOf(result.toUpperCase(Locale.ROOT));
        }

        return Optional.ofNullable(gender);
    }

    @Override
    public Optional<Member> getMember() {
        return memberRepository.findByKakaoIdAndNotLeave(getId());
    }
}
