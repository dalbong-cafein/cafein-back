package com.dalbong.cafein.util;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class CookieUtil {

    //TODO setHttpOnly, Secure
    //쿠키 create
    public void createCookie(HttpServletResponse response, String cookieName, String value, int maxAge){
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setHttpOnly(false);
        cookie.setSecure(false);
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");

        response.addCookie(cookie);
    }

    //쿠키를 가져온다
    public Cookie getCookie(HttpServletRequest request, String cookieName){
        Cookie[] cookies = request.getCookies();
        if(cookies==null) {
            return null;
        }

        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);

    }
}
