package com.dalbong.cafein.dto.login;

import com.dalbong.cafein.domain.member.AuthProvider;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountUniteRegDto {

    private String newOAuthId;

    private AuthProvider newAuthProvider;
}
