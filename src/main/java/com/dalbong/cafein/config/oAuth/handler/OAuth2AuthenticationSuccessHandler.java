package com.dalbong.cafein.config.oAuth.handler;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.redis.RedisService;
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

        //TODO header, cookie 고민
        response.setHeader("accessToken", accessToken);
        response.setHeader("refreshToken", refreshToken);

        System.out.println(response.getHeaderNames());
        System.out.println(redirectUrl);
        //redirect
        //getRedirectStrategy().sendRedirect(request,response,redirectUrl);
    }
}
