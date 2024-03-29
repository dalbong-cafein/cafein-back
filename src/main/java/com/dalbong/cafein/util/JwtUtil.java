package com.dalbong.cafein.util;

import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Date;

@Component
@Log4j2
public class JwtUtil implements Serializable {

    @Value("${app.token.secretKey}")
    private String secretKey;

    //배포용
    public final static int accessTokenExpire = 60*10;
    public final static int refreshTokenExpire = 60*60*24*7;

    //테스트용
    public final static int accountUniteTokenExpire = 10*600;
    //public final static int accessTokenExpire = 10*600;
    //public final static int refreshTokenExpire = 20160;

    public final static String accountUniteTokenName = "accountUniteToken";

    public final static String accessTokenName = "accessToken";

    public final static String refreshTokenName = "refreshToken";

    //accountUniteToken 생성
    public String generateAccountUniteToken(String email) {

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(accountUniteTokenExpire).toInstant()))
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //accessToken 생성
    public String generateAccessToken(Long memberId) {

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(accessTokenExpire).toInstant()))
                .claim("memberId", memberId)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //refreshToken 생성
    public String generateRefreshToken(Long memberId) {

        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(refreshTokenExpire).toInstant()))
                .claim("memberId", memberId)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    //accountUniteToken 토큰 검증
    public String validateAndExtractAccountUniteToken(String tokenStr){
        try{
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(tokenStr).getBody();
            String email = claims.get("email", String.class);
            return email;
        }catch (MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return null;
    }

    //accessToken, refreshToken 토큰 검증
    public Long validateAndExtractLoginToken(String tokenStr){
        try{
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(tokenStr).getBody();
            Long memberId= claims.get("memberId", Long.class);
            return memberId;
        }catch (MalformedJwtException e) {
            log.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT 토큰이 잘못되었습니다.");
        }
        return null;
    }
}
