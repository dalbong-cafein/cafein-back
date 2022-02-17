package com.dalbong.cafein.config.oAuth.handler;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.redis.RedisService;
import com.dalbong.cafein.util.CookieUtil;
import com.dalbong.cafein.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Value("${app.oauth2.authorized-redirect-url}")
    private String redirectUrl;

    private final JwtUtil jwtUtil;
    private final RedisService redisService;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws  IOException {
        System.out.println("성공!!!!!!!");

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        //accessToken, refreshToken 토큰 생성
        String accessToken = jwtUtil.generateAccessToken(principalDetails.getMember().getMemberId());
        String refreshToken = jwtUtil.generateRefreshToken(principalDetails.getMember().getMemberId());

        //refreshToken - redis 에 저장
        redisService.setValues(principalDetails.getMember().getMemberId(), refreshToken);

        //TODO deploy - setMax() modify
        cookieUtil.createCookie(response, jwtUtil.accessTokenName, accessToken, jwtUtil.accessTokenExpire);
        cookieUtil.createCookie(response, jwtUtil.refreshTokenName, refreshToken,jwtUtil.refreshTokenExpire);

        //redirect
        getRedirectStrategy().sendRedirect(request,response,redirectUrl);
    }
}
