package com.dalbong.cafein.dto.admin.member;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberState;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminMemberResDto {

    private Long memberId;

    private String nickname;

    private ImageDto memberImageDto;

    private List<AuthProvider> socialTypeList;

    private String phone;

    private String email;

    private MemberState memberState;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    private Long memoId;

    public AdminMemberResDto(Member member, ImageDto memberImageDto, Long memoId){
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.memberImageDto = memberImageDto;;
        this.phone = member.getPhone();
        this.email = member.getEmail();
        this.memberState = member.getState() != null ? member.getState() : null;
        this.memoId = memoId;
        this.regDateTime = member.getRegDateTime();

        //소셜타입
        this.socialTypeList = new ArrayList<>();

        if(member.getKakaoId() != null && !member.getKakaoId().isBlank()){
            this.socialTypeList.add(AuthProvider.KAKAO);
        }

        if(member.getNaverId() != null && !member.getNaverId().isBlank()){
            this.socialTypeList.add(AuthProvider.NAVER);
        }

        if(member.getAppleId() != null && !member.getAppleId().isBlank()){
            this.socialTypeList.add(AuthProvider.APPLE);
        }
    }

}
