package com.dalbong.cafein.dto.login;

import com.dalbong.cafein.domain.member.AuthProvider;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoginDto {

    @NotNull
    private AuthProvider authProvider;

    @NotBlank
    private String authToken;

    private String username;

}
