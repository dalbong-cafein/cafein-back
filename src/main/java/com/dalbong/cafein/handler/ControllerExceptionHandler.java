package com.dalbong.cafein.handler;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.handler.exception.AlreadyExistedAccountException;
import com.dalbong.cafein.handler.exception.CustomException;
import com.dalbong.cafein.handler.exception.CustomValidationException;
import com.dalbong.cafein.handler.exception.StickerExcessException;
import com.dalbong.cafein.redis.RedisService;
import com.dalbong.cafein.util.CookieUtil;
import com.dalbong.cafein.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.sql.SQLIntegrityConstraintViolationException;

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

    //sql 에러
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<CMRespDto<?>> apiSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException e){
        return new ResponseEntity<>(new CMRespDto<>(-1,e.getMessage(),null), HttpStatus.BAD_REQUEST);
    }

    //스티커 보유량 초과 에러
    @ExceptionHandler(StickerExcessException.class)
    public ResponseEntity<CMRespDto<?>> exceedStickerException(StickerExcessException e){

        return new ResponseEntity<>(new CMRespDto<>(-1, e.getMessage(), null), HttpStatus.INTERNAL_SERVER_ERROR);

    }

    //파일 최대 사이즈 초과 에러
    @ExceptionHandler(FileSizeLimitExceededException.class)
    public ResponseEntity<CMRespDto<?>> fileSizeLimitExceededException(FileSizeLimitExceededException e){
        return new ResponseEntity<>(new CMRespDto<>(-1, "파일 최대 사이즈를 초과하였습니다.", null), HttpStatus.BAD_REQUEST);
    }

}
