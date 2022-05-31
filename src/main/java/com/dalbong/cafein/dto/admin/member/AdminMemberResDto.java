package com.dalbong.cafein.dto.admin.member;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminMemberResDto {

    private Long memberId;

    private String nickname;

    private List<AuthProvider> socialTypeList;

    private String phone;

    private String email;

    private Boolean isDeleted;

    private Boolean isReported;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime regDateTime;

}
