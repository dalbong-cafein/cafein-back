package com.dalbong.cafein.handler.exception;

import com.dalbong.cafein.dto.login.AccountUniteResDto;
import org.springframework.security.core.AuthenticationException;

public class AlreadyExistedAccountException extends AuthenticationException {

    private static final long serialVersionUID=1L;

    private final AccountUniteResDto accountUniteResDto;

    public AlreadyExistedAccountException(String message, AccountUniteResDto accountUniteResDto) {
        super(message);
        this.accountUniteResDto = accountUniteResDto;
    }

    public AccountUniteResDto getAccountUniteResDto(){
        return accountUniteResDto;
    }
}
