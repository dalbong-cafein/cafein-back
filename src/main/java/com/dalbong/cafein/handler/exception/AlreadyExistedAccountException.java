package com.dalbong.cafein.handler.exception;

import com.dalbong.cafein.dto.login.UniteAccountResDto;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

public class AlreadyExistedAccountException extends AuthenticationException {

    private static final long serialVersionUID=1L;

    private final UniteAccountResDto uniteAccountResDto;

    public AlreadyExistedAccountException(String message, UniteAccountResDto uniteAccountResDto) {
        super(message);
        this.uniteAccountResDto = uniteAccountResDto;
    }

    public UniteAccountResDto getUniteAccountResDto(){
        return uniteAccountResDto;
    }
}
