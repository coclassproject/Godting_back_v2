package com.gts.godtingDev.config.auth.token;

import com.gts.godtingDev.config.auto.CookieConfig;
import com.gts.godtingDev.config.exception.CustomException;
import com.gts.godtingDev.config.exception.ExceptionMessage;
import com.gts.godtingDev.config.oauth2.OAuth2UserDetailsService;
import com.gts.godtingDev.config.oauth2.authentication.OAuth2UserDetails;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieConfig cookieConfig;
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtTokenProvider.resolveToken(request);
        String refreshToken = null;

        // 엑세스 토큰 검증
        if (accessToken != null) {
            if (jwtTokenProvider.verifyToken(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                refreshToken = cookieConfig.getCookie(request, "authorization").getValue();
            }
        }

        try {
            Optional<JwtToken> jwtTokenEntity = jwtTokenRepository.findByRefreshToken(refreshToken);
            JwtToken token = jwtTokenEntity.get();

            if(token != null && jwtTokenProvider.verifyToken(token.getRefreshToken()) && jwtTokenProvider.verifyToken(refreshToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                OAuth2UserDetails oAuth2UserDetails = (OAuth2UserDetails) authentication.getPrincipal();
                response.addHeader("Authorization", jwtTokenProvider.accessToken(authentication));
            }
        } catch (ExpiredJwtException e) {
            throw new CustomException(ExceptionMessage.INVALID_REFRESH_TOKEN);
        }

    }


}
