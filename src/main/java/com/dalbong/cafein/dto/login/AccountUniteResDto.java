package com.dalbong.cafein.dto.login;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class AccountUniteResDto {

    private String email;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime joinDateTime;

    private String newOAuthId;

    private AuthProvider newAuthProvider;
}
