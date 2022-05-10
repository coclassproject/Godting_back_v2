package com.gts.godting.config.auth.token;


import com.gts.godting.config.auth.UserDetailsImpl;
import com.gts.godting.config.auth.UserDetailsServiceImpl;
import com.gts.godting.config.auto.CookieConfig;
import com.gts.godting.config.exception.CustomException;
import com.gts.godting.config.exception.ExceptionMessage;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final CookieConfig cookieConfig;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String accessToken = jwtTokenProvider.resolveToken((HttpServletRequest) request, "ACCESS");
        String refreshToken = null;

        if(accessToken != null) {
            UserDetailsImpl userDetailsImpl = (UserDetailsImpl) userDetailsServiceImpl.loadUserByUsername(jwtTokenProvider.getUserEmail(accessToken));
            if (jwtTokenProvider.verifyToken(accessToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                refreshToken = cookieConfig.getCookie((HttpServletRequest) request, "X-AUTH-REFRESH-TOKEN").getValue();
            }
        }

        try {
            RefreshToken token = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (token != null && jwtTokenProvider.verifyToken(token.getRefreshToken()) && jwtTokenProvider.verifyToken(refreshToken)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(refreshToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
                ((HttpServletResponse) response).addHeader("X-AUTH-REFRESH-TOKEN", jwtTokenProvider.accessToken(userDetails.getUsername()));
            }
        } catch (ExpiredJwtException e) {
            log.info("refreshToken : ExpiredJwtException {}", refreshToken);
            throw new CustomException(ExceptionMessage.INVALID_REFRESH_TOKEN);
        }

        chain.doFilter(request, response);
    }
}
