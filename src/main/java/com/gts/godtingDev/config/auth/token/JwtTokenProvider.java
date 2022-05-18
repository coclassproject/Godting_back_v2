package com.gts.godtingDev.config.auth.token;

import com.gts.godtingDev.config.exception.CustomException;
import com.gts.godtingDev.config.exception.ExceptionMessage;
import com.gts.godtingDev.config.oauth2.authentication.OAuth2UserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
public class JwtTokenProvider {

    @Value("${spring.jwt.secret}")
    private String secretKey;

    private long accessTokenValidTime = 1000L * 60L * 60L; // 1시간
    private long refreshTokenValidTime = 1000L * 60L * 60L * 24L * 30L; // 30일(1달)

    public String createToken(Authentication authentication, Long tokenValidTime) {
        OAuth2UserDetails principal = (OAuth2UserDetails) authentication.getPrincipal();
        Claims claims = Jwts.claims().setSubject(principal.getSocialId());
        claims.put("socialType", principal.getSocialType());

        Date date = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(date)
                .setExpiration(new Date(date.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();

    }

    public String accessToken(Authentication authentication) {
        return createToken(authentication, accessTokenValidTime);
    }

    public String refreshToken(Authentication authentication) {
        return createToken(authentication, refreshTokenValidTime);
    }


    public boolean verifyToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);

            return claimsJws.getBody()
                    .getExpiration()
                    .after(new Date());
        } catch (Exception e) {
            throw new CustomException(ExceptionMessage.INVALID_REFRESH_TOKEN);
        }
    }

    public String resolveToken(HttpServletRequest request, String tokenName) {
        return request.getHeader("Authorization");
    }

    public String getUserSocialId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

}
