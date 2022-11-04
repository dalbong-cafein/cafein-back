package com.dalbong.cafein.dto.member;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberState;
import com.dalbong.cafein.dto.image.ImageDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberInfoDto {

    private Long memberId;

    private String nickname;

    private ImageDto imageDto;

    private String phone;

    private String email;

    private LocalDate birth;

    private MemberState state;

    private AuthProvider mainAuthProvider;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime joinDateTime;

    private Boolean isAgreeLocation;

    private Boolean isAgreeMarketingPush;

    public MemberInfoDto(Member member, ImageDto imageDto){
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname() != null ? member.getNickname() : null;
        this.phone = member.getPhone() != null ? member.getPhone() : null;
        this.email = member.getEmail();
        this.birth = member.getBirth();
        this.state = member.getState();
        this.imageDto = imageDto;
        this.mainAuthProvider = member.getMainAuthProvider();
        this.joinDateTime = member.getRegDateTime();
        this.isAgreeLocation = member.getIsAgreeLocation();
        this.isAgreeMarketingPush = member.getIsAgreeMarketingPush();
    }
}
