package com.dalbong.cafein.dto.admin.member;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
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

    private Boolean isDeleted;

    private Boolean isReported;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

    public AdminMemberResDto(Member member, ImageDto memberImageDto, Boolean isReported){
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.memberImageDto = memberImageDto;;
        this.phone = member.getPhone();
        this.email = member.getEmail();
        this.isDeleted = member.getIsDeleted();
        this.isReported = isReported;
        //소셜타입

        this.socialTypeList = new ArrayList<>();

        if(member.getKakaoId() != null){
            this.socialTypeList.add(AuthProvider.KAKAO);
        }

        if(member.getNaverId() != null){
            this.socialTypeList.add(AuthProvider.NAVER);
        }



    }

}
