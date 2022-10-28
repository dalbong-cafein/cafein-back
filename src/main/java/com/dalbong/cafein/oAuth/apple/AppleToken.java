package com.dalbong.cafein.oAuth.apple;

import lombok.Data;

@Data
public class AppleToken {

    private String access_token;
    private String expires_in;
    private String id_token;
    private String refresh_token;
    private String token_type;
    private String error;
}
