package com.dalbong.cafein.controller;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.redis.RedisService;
import com.dalbong.cafein.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final RedisService redisService;
    private final JwtUtil jwtUtil;

    @GetMapping("/auth/refreshToken")
    public ResponseEntity<?> verifyRefreshToken(@RequestHeader("refreshToken") String refreshToken, HttpServletResponse response){

        //refreshToken 검증
        Long memberId = jwtUtil.validateAndExtract(refreshToken);

        //잘못된 refreshToken 일 경우
        if (memberId == null){
            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null), HttpStatus.UNAUTHORIZED);
        }

        //redis 에 등록된 refreshToken 가져오기
        String findRefreshToken = redisService.getValues(memberId);

        System.out.println(refreshToken);
        System.out.println(findRefreshToken);

        //잘못된 refreshToken 일 경우
        if(findRefreshToken == null || !findRefreshToken.equals(refreshToken) || refreshToken == null){
            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null),HttpStatus.UNAUTHORIZED);
        }

        //새로운 accessToken 생성
        String newAccessToken = jwtUtil.generateAccessToken(memberId);

        //TODO header, cookie 고민
        response.setHeader("accessToken", newAccessToken);

        return new ResponseEntity<>(new CMRespDto<>(1,"accessToken 토큰 재발급 완료", null), HttpStatus.OK);
    }

    @GetMapping("/auth/test")
    public String test(){
        return "성공했습니다.";
    }
}
