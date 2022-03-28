package com.dalbong.cafein.controller;

import lombok.Data;

@Data
public class NaverOAuthToken {

    private String access_token;

    private String token_type;

    private String refresh_token;

    private int expires_in;

    private String scope;

    private int refresh_token_expires_in;
}
