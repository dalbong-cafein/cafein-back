package com.dalbong.cafein.filter;

import com.dalbong.cafein.config.auth.PrincipalDetails;
import com.dalbong.cafein.domain.member.Member;
import com.dalbong.cafein.domain.member.MemberRepository;
import com.dalbong.cafein.util.CookieUtil;
import com.dalbong.cafein.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PatternMatchUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Log4j2
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private JwtUtil jwtUtil;
    private CookieUtil cookieUtil;
    private MemberRepository memberRepository;

    private static final String[] pattern = {"/", "/auth/*","/login/*"};

    @Autowired
    public JwtAuthorizationFilter(CookieUtil cookieUtil, JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.cookieUtil = cookieUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        System.out.println(request.getRequestURI());
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0){
            for (Cookie cookie : cookies){
                System.out.println("-------------------");
                System.out.println(cookie.getName());
                System.out.println(cookie.getValue());

            }
        }


        //인증이 필요 없는 uri 일 경우 바로 통과
        if (PatternMatchUtils.simpleMatch(pattern,request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        System.out.println("인증필터 시작!!");

        System.out.println(request.getRequestURI());
        System.out.println(request.getRequestURL().toString());

        Cookie cookie = cookieUtil.getCookie(request, jwtUtil.accessTokenName);
        //accessToken 쿠키가 존재할 경우
        if (cookie != null) {
            String accessToken = cookie.getValue();
            Long memberId = jwtUtil.validateAndExtractLoginToken(accessToken);

            //accessToken 이 유효할 경우
            if (memberId != null) {
                System.out.println("memberId: " + memberId);
                Member member = memberRepository.findWithRoleSetByMemberId(memberId).orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않은 회원입니다."));

                PrincipalDetails principalDetails = new PrincipalDetails(member);

                //Authentication 객체 만들기
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                //시큐리티의 세션에 접근하여 Authentication 객체를 저장.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        }

        chain.doFilter(request, response);
    }

}
