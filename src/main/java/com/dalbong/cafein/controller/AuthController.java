package com.dalbong.cafein.controller;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.login.AccountUniteRegDto;
import com.dalbong.cafein.redis.RedisService;
import com.dalbong.cafein.service.member.MemberService;
import com.dalbong.cafein.service.sms.SmsService;
import com.dalbong.cafein.util.CookieUtil;
import com.dalbong.cafein.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final RedisService redisService;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;
    private final SmsService smsService;
    private final MemberService memberService;

    /**
     * 계정 연동
     */
    @PatchMapping("/auth/account-unite")
    public ResponseEntity<?> uniteAccount(@CookieValue("accountUniteToken") String accountUniteToken,
                                          @RequestBody AccountUniteRegDto accountUniteRegDto,
                                          HttpServletResponse response){

        String email = jwtUtil.validateAndExtractAccountUniteToken(accountUniteToken);

        //잘못된 accountUniteToken 일 경우
        if (email == null){
            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null), HttpStatus.UNAUTHORIZED);
        }

        //계정 통합
        Long memberId = memberService.uniteAccount(email, accountUniteRegDto);

        //accessToken, refreshToken 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(memberId);
        String refreshToken = jwtUtil.generateRefreshToken(memberId);

        //refreshToken - redis 에 저장
        redisService.setValues(memberId, refreshToken);

        //TODO deploy - setMax() modify
        cookieUtil.createCookie(response, JwtUtil.accessTokenName, accessToken, JwtUtil.accessTokenExpire);
        cookieUtil.createCookie(response, JwtUtil.refreshTokenName, refreshToken, JwtUtil.refreshTokenExpire);

        return new ResponseEntity<>(new CMRespDto<>(1,"계정 통합 성공", null), HttpStatus.OK);
    }

    /**
     * 닉네임 중복확인
     */
    @GetMapping("/auth/duplicate-nickname")
    public ResponseEntity<?> CheckDuplicate(@RequestParam("nickname") String nickname){

        Boolean result = memberService.isDuplicateNickname(nickname);

        return new ResponseEntity<>(new CMRespDto<>(1,"닉네임 중복체크 성공",result), HttpStatus.OK);
    }


    /**
     * sms 인증 번호 전송
     */
    @GetMapping("/auth/send-sms")
    public ResponseEntity<?> sendSms(@RequestParam("toNumber") String toNumber){
        System.out.println(toNumber);

        //랜덤 4자리 인증번호 생성
        String certifyNum = smsService.createCertifyNum();

        //sms 문자 메시지 전송
        smsService.sendSms(toNumber, certifyNum);

        return new ResponseEntity<>(new CMRespDto<>(1,"문자 메세지 전송 성공", certifyNum), HttpStatus.OK);
    }

    /**
     * accessToken 재발급
     */
    @GetMapping("/auth/refreshToken")
    public ResponseEntity<?> verifyRefreshToken(@CookieValue("refreshToken") String refreshToken, HttpServletResponse response){

        //refreshToken 검증
        Long memberId = jwtUtil.validateAndExtractLoginToken(refreshToken);

        //잘못된 refreshToken 일 경우
        if (memberId == null){
            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null), HttpStatus.UNAUTHORIZED);
        }

        //redis 에 등록된 refreshToken 가져오기
        String findRefreshToken = redisService.getValues(memberId);

        //잘못된 refreshToken 일 경우
        if(findRefreshToken == null || !findRefreshToken.equals(refreshToken)){
            return new ResponseEntity<>(new CMRespDto<>(1,"UnAuthorized", null),HttpStatus.UNAUTHORIZED);
        }

        //새로운 accessToken 생성
        String newAccessToken = jwtUtil.generateAccessToken(memberId);

        cookieUtil.createCookie(response, jwtUtil.accessTokenName, newAccessToken, jwtUtil.accessTokenExpire);

        return new ResponseEntity<>(new CMRespDto<>(1,"accessToken 토큰 재발급 완료", null), HttpStatus.OK);
    }

    @GetMapping("/auth/test")
    public String test(HttpServletRequest request){
        System.out.println("----------------");
        System.out.println(request.getRequestURI());
        String test = request.getHeader("test");
        System.out.println("헤더값: " + test);


        Cookie[] cookies = request.getCookies();
        System.out.println("쿠키 개수: " + cookies.length);
        for (Cookie cookie :cookies){
            System.out.println("cookie name: "+ cookie.getName());
            System.out.println("cookie value: "+ cookie.getValue());
        }

        return "성공했습니다";
    }
}
