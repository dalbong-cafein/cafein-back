package com.dalbong.cafein.config;

import com.dalbong.cafein.config.security.JwtAccessDeniedHandler;
import com.dalbong.cafein.config.security.JwtAuthenticationEntryPoint;
import com.dalbong.cafein.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthorizationFilter jwtAuthorizationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers(
                        "/h2-console/**",
                        "/favicon.ico",
                        "/.env"
                );

        }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지 않겠다.
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable();

        http
                .authorizeRequests()
                .antMatchers(
                        //비로그인 조회 관련 url
                        HttpMethod.GET,

                        //카페 관련
                        "/stores",
                        "/stores/{storeId}/near-stores",
                        "/stores/{storeId}",

                        //리뷰 관련
                        "/stores/{storeId}/detail-review-score",
                        "/stores/{storeId}/reviews",
                        "/stores/{storeId}/reviews/limit",

                        //혼잡도 관련
                        "/stores/{storeId}/congestion"
                ).permitAll()
                .antMatchers(
                        // 인증 관련
                        "/auth/logout",

                        // 회원 관련
                        "/members", "/members/**",

                        // 카페 관련
                        "/stores", "/stores/**",

                        // 리뷰 관련
                        "/reviews","/reviews/**", "/stores/{storeId}/reviews",

                        // 신고 관련
                        "/reviews/**/reports",

                        // 하트 관련
                        "/hearts", "/hearts/**", "/stores/{storeId}/hearts",

                        // 쿠폰 관련
                        "/coupons",

                        // 혼잡도 관련
                        "/congestion","/congestion/**",

                        // 신고 관련
                        "/stickers", "/stickers/**",

                        // 게시글 관련
                        "/boards",

                        // 알림 관련
                        "/notices", "/notices/**"
                         ).authenticated()
                //.antMatchers("/admin/**/*").access("hasRole('ROLE_ADMIN')")
                //.antMatchers("/data/*").access("hasRole('ROLE_ADMIN')")
                .antMatchers("/web/**").permitAll()
                .anyRequest().permitAll()

                .and()
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler);

    }
}
