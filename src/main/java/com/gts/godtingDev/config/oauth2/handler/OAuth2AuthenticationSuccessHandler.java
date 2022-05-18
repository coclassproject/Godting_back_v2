package com.gts.godtingDev.config.oauth2.handler;

import com.gts.godtingDev.config.auth.token.JwtToken;
import com.gts.godtingDev.config.auth.token.JwtTokenProvider;
import com.gts.godtingDev.config.auth.token.JwtTokenRepository;
import com.gts.godtingDev.config.auto.CookieConfig;
import com.gts.godtingDev.config.oauth2.authentication.OAuth2UserDetails;
import com.gts.godtingDev.user.oauth2.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${front.redirect.url}")
    private String redirectUrl;

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieConfig cookieConfig;
    private final JwtTokenRepository jwtTokenRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2UserDetails principal = (OAuth2UserDetails) authentication.getPrincipal();
        JwtToken jwtToken;

        if (authentication.getAuthorities().stream().anyMatch(s -> s.getAuthority().equals(Role.GUEST.getGratedAuthority()))){
            getRedirectStrategy().sendRedirect(request, response, redirectUrl + "sign_in?social_type=" +
                    principal.getSocialType() + "&social_id=" +
                    principal.getSocialId());
        } else {
            response.setHeader("Authorization", jwtTokenProvider.accessToken(authentication));
            jwtToken = new JwtToken(principal.getSocialId(), jwtTokenProvider.refreshToken(authentication));
            jwtTokenRepository.save(jwtToken);
            response.addCookie(cookieConfig.addCookie("refreshToken", jwtToken.getRefreshToken()));
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
    }
}
