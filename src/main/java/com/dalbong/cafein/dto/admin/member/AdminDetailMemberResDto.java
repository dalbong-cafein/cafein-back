package com.dalbong.cafein.dto.admin.member;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.GenderType;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminDetailMemberResDto {

    private Long memberId;

    private String nickname;

    private ImageDto memberImageDto;

    private List<AuthProvider> socialTypeList;

    private String phone;

    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private LocalDate birth;

    private GenderType gender;

    private long heartCnt;

    private long congestionCnt;

    private long reviewCnt;

    private long stickerCnt;

    private Boolean isDeleted;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime leaveDateTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime joinDateTime;

    public AdminDetailMemberResDto(Member member, ImageDto memberImageDto,
                                   long heartCnt, long congestionCnt, long reviewCnt, long stickerCnt){

        this.memberId = member.getMemberId();
        this.nickname = member.getNickname();
        this.memberImageDto = memberImageDto;
        this.phone = member.getPhone();
        this.email = member.getEmail();
        this.birth = member.getBirth();
        this.gender = member.getGenderType() != null ? member.getGenderType() : null;
        this.heartCnt = heartCnt;
        this.congestionCnt = congestionCnt;
        this.reviewCnt = reviewCnt;
        this.stickerCnt = stickerCnt;
        this.isDeleted = member.getIsDeleted();
        this.leaveDateTime = member.getLeaveDateTime() != null ? member.getLeaveDateTime() : null;
        this.joinDateTime = member.getRegDateTime();

        //소설 타입
        this.socialTypeList = new ArrayList<>();

        if(member.getKakaoId() != null){
            this.socialTypeList.add(AuthProvider.KAKAO);
        }

        if(member.getNaverId() != null){
            this.socialTypeList.add(AuthProvider.NAVER);
        }
    }


}
