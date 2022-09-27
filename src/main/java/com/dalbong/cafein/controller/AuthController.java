package com.dalbong.cafein.controller;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.domain.member.AuthProvider;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.login.AccountUniteRegDto;
import com.dalbong.cafein.dto.login.LoginDto;
import com.dalbong.cafein.dto.member.MemberInfoDto;
import com.dalbong.cafein.oAuth.SocialLoginService;
import com.dalbong.cafein.redis.RedisService;
import com.dalbong.cafein.service.member.MemberService;
import com.dalbong.cafein.service.sms.SmsService;
import com.dalbong.cafein.util.CookieUtil;
import com.dalbong.cafein.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

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
    private final SocialLoginService socialLoginService;

    /**
     * 소셜 로그인
     */
    @PostMapping("/auth/social-login")
    public ResponseEntity<?> oAuthLogin(@Validated @RequestBody LoginDto loginDto, BindingResult bindingResult,
                                        HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {

        //Client Ip
        String clientIp = getClientRemoteIp(request);

        //로그인 진행
        Member member = socialLoginService.login(loginDto, clientIp);

        //accessToken, refreshToken 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(member.getMemberId());
        String refreshToken = jwtUtil.generateRefreshToken(member.getMemberId());

        //refreshToken - redis 에 저장
        redisService.setValues(member.getMemberId(), refreshToken);

        //TODO deploy - setMax() modify
        cookieUtil.createCookie(response, jwtUtil.accessTokenName, accessToken, jwtUtil.accessTokenExpire);
        cookieUtil.createCookie(response, jwtUtil.refreshTokenName, refreshToken,jwtUtil.refreshTokenExpire);

        //회원 정보 조회
        MemberInfoDto memberInfoDto = memberService.getMemberInfo(member.getMemberId());

        return new ResponseEntity<>(new CMRespDto<>(1,"소셜 로그인 성공",memberInfoDto),HttpStatus.OK);
    }

    private String getClientRemoteIp(HttpServletRequest request) {

        String ip = request.getHeader("X-FORWARDED-FOR");

        //proxy 환경일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        //웹로직 서버일 경우
        if (ip == null || ip.length() == 0) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0) {
            ip = request.getRemoteAddr() ;
        }

        return ip;
    }

    /**
     * 로그아웃
     */
    @PostMapping("/auth/logout")
    public ResponseEntity<?> logout(HttpServletResponse response,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails){

        redisService.delValues(principalDetails.getMember().getMemberId());

        cookieUtil.createCookie(response, jwtUtil.accessTokenName, null, 0);
        cookieUtil.createCookie(response, jwtUtil.refreshTokenName, null,0);

        return new ResponseEntity<>(new CMRespDto<>(1, "로그아웃 성공",null), HttpStatus.OK);
    }

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

        //회원 정보 조회
        MemberInfoDto memberInfoDto = memberService.getMemberInfo(memberId);

        return new ResponseEntity<>(new CMRespDto<>(1,"계정 통합 성공", memberInfoDto), HttpStatus.OK);
    }

    /**
     * 닉네임 중복확인
     */
    @GetMapping("/auth/duplicate-nickname")
    public ResponseEntity<?> checkDuplicate(@RequestParam("nickname") String nickname){

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



    @GetMapping("/login/kakao3")
    public String testAuth(@RequestParam("code") String code) throws JsonProcessingException {
        System.out.println(code);

        //POST 방식으로 key=value 데이터를 요청 (카카오쪽으로)
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성 (엔티티) - 헤더, 바디
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "authorization_code");
        params.add("client_id", "30e9f4fa92d2521d41eaf6f419dd5185");
        params.add("redirect_uri", "http://localhost:5000/login/kakao3");
        params.add("code", code);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //Http요청하기 - Post방식으로 -그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);

        System.out.println(oAuthToken.getAccess_token());

        return oAuthToken.getAccess_token();
    }

    @GetMapping("/login/naver3")
    public String testNaverAuth(@RequestParam("code") String code) throws JsonProcessingException {
        System.out.println(code);

        //POST 방식으로 key=value 데이터를 요청
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성 (엔티티) - 헤더, 바디
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");


        //HttpBody 오브젝트 생성
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        params.add("client_id", "iNA8YL47sprlUNIIxhnK");
        params.add("client_secret", "8q9aoiAq_W");
        params.add("redirect_uri", "http://localhost:5000/login/naver3");
        params.add("code", code);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);

        //Http요청하기 - Post방식으로 -그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code" +
                        "&client_id=iNA8YL47sprlUNIIxhnK&client_secret=8q9aoiAq_W" +
                        "&redirect_uri=http://localhost:5000/login/naver3" +
                        "&code=" + code,
                HttpMethod.GET,
                naverTokenRequest,
                String.class);


        System.out.println(response.getBody());
        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);

        System.out.println(oAuthToken.getAccess_token());

        return oAuthToken.getAccess_token();
    }
}
