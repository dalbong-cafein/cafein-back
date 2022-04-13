package com.dalbong.cafein.dto.member;

import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.dto.image.ImageDto;
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

    private LocalDateTime joinDateTime;

    public MemberInfoDto(Member member, ImageDto imageDto){
        this.memberId = member.getMemberId();
        this.nickname = member.getNickname() != null ? member.getNickname() : null;
        this.phone = member.getPhone() != null ? member.getPhone() : null;
        this.email = member.getEmail();
        this.birth = member.getBirth();
        this.imageDto = imageDto;
        this.joinDateTime = member.getRegDateTime();
    }
}
