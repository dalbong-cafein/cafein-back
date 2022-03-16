package com.dalbong.cafein.config.oAuth.handler;

import com.dalbong.cafein.dto.CMRespDto;
import com.dalbong.cafein.dto.login.UniteAccountResDto;
import com.dalbong.cafein.handler.exception.AlreadyExistedAccountException;
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

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        if (exception instanceof AlreadyExistedAccountException){
            //Content-Type: application/json
            response.setHeader("content-type", "application/json");
            response.setCharacterEncoding("utf-8");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            AlreadyExistedAccountException alreadyExistedAccountException = (AlreadyExistedAccountException) exception;
            CMRespDto<UniteAccountResDto> cmRespDto =
                    new CMRespDto<>(-1, exception.getMessage(),
                            alreadyExistedAccountException.getUniteAccountResDto());

            String result = objectMapper.writeValueAsString(cmRespDto);
            response.getWriter().write(result);
            }
        else{
            response.sendError(500, "로그인 시도 중 에러 발생");
        }
    }
}
