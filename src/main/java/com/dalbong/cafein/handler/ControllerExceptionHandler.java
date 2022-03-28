package com.dalbong.cafein.handler;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.handler.exception.AlreadyExistedAccountException;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.handler.exception.CustomValidationException;
import com.dalbong.cafein.redis.RedisService;
import com.dalbong.cafein.util.CookieUtil;
import com.dalbong.cafein.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@ControllerAdvice
@RequiredArgsConstructor
public class ControllerExceptionHandler {

    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    //유효성 검사
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<CMRespDto<?>> validationApiException(CustomValidationException e){

        return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),e.errorMap), HttpStatus.BAD_REQUEST);
    }

    //에러 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CMRespDto<?>> apiException(CustomException e){

        return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null), HttpStatus.BAD_REQUEST);
    }

    //사용중인 email 계정 - 계정 연동
    @ExceptionHandler(AlreadyExistedAccountException.class)
    public ResponseEntity<CMRespDto<?>> alreadyExistedAccountException(AlreadyExistedAccountException e,
                                                                       HttpServletResponse response){
        //accountUniteToken 생성
        String accountUniteToken = jwtUtil.generateAccountUniteToken(e.getAccountUniteResDto().getEmail());

        //계정 통합 쿠키 생성
        cookieUtil.createCookie(response, JwtUtil.accountUniteTokenName, accountUniteToken, JwtUtil.accountUniteTokenExpire);

        return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),e.getAccountUniteResDto()), HttpStatus.UNAUTHORIZED);
    }

}
