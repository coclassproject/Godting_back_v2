package com.gts.godting.config.auth.token;

import com.gts.godting.config.auth.UserDetailsServiceImpl;
import com.gts.godting.config.exception.CustomException;
import com.gts.godting.config.exception.ExceptionMessage;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Service
@PropertySource("classpath:application-dev.properties")
@Slf4j
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.secret.key}")
    private String secretKey;

    private long accessTokenValidTime = 1000L * 60L * 60L; // 1시간
    private long refreshTokenValidTime = 1000L * 60L * 60L * 24L * 30L; // 30일(1달)

    private final UserDetailsServiceImpl userDetailsService;

    public String createToken(String email, Long tokenValidTime) {
        Claims claims = Jwts.claims().setSubject(email);

        Date date = new Date();

        return Jwts.builder()
                        .setClaims(claims)
                        .setIssuedAt(date)
                        .setExpiration(new Date(date.getTime() + tokenValidTime))
                        .signWith(SignatureAlgorithm.HS256, secretKey)
                        .compact();

    }

    public String accessToken(String email) {
        return createToken(email, accessTokenValidTime);
    }

    public String refreshToken(String email) {
        return createToken(email, refreshTokenValidTime);
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
        return request.getHeader("X-AUTH-" + tokenName + "-TOKEN");
    }

    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserEmail(token));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String getUserEmail(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}