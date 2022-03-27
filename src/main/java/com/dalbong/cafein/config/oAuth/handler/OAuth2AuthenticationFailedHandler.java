package com.dalbong.cafein.config.oAuth.handler;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.login.AccountUniteResDto;
import com.dalbong.cafein.handler.exception.AlreadyExistedAccountException;
import com.dalbong.cafein.util.CookieUtil;
import com.dalbong.cafein.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationFailedHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final CookieUtil cookieUtil;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof AlreadyExistedAccountException){
            //Content-Type: application/json
            response.setHeader("content-type", "application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value()); //401에러

            AlreadyExistedAccountException alreadyExistedAccountException = (AlreadyExistedAccountException) exception;
            AccountUniteResDto accountUniteResDto = alreadyExistedAccountException.getAccountUniteResDto();

            //accountUniteToken 생성
            String accountUniteToken = jwtUtil.generateAccountUniteToken(accountUniteResDto.getEmail());

            //계정 통합 쿠키 생성
            cookieUtil.createCookie(response, JwtUtil.accountUniteTokenName, accountUniteToken, JwtUtil.accountUniteTokenExpire);

            CMRespDto<AccountUniteResDto> cmRespDto =
                    new CMRespDto<>(-1, exception.getMessage(), accountUniteResDto);

            String result = objectMapper.writeValueAsString(cmRespDto);
            response.getWriter().write(result);

            }
        else{
            System.out.println(exception.getMessage());
            exception.getMessage();
            response.sendError(500, "로그인 시도 중 에러 발생");
        }
    }
}
