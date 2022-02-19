package com.dalbong.cafein.config;

import com.dalbong.cafein.config.oAuth.OAuth2DetailsService;
import com.dalbong.cafein.config.oAuth.handler.OAuth2AuthenticationSuccessHandler;
import com.dalbong.cafein.domain.member.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final OAuth2DetailsService oAuth2DetailsService;
    private final OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Bean
    public PasswordEncoder encode() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //세션을 사용하지 않겠다.
                .and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable();

        http.oauth2Login()
                .userInfoEndpoint()
                .userService(oAuth2DetailsService)
                .and()
                .successHandler(oAuth2AuthenticationSuccessHandler)

                .and()
                .authorizeRequests()
                .antMatchers("/stores/{storeId}/isApproval").access("hasRole('ROLE_ADMIN')")
                //.antMatchers("/stores/{storeId}/isApproval").hasRole("ADMIN")
                .anyRequest().permitAll();
    }
}
