package com.gts.godting.config.oauth2;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        if (principal.getUser() == null) {
            log.info("회원없을때 - 회원가입페이지");
            getRedirectStrategy().sendRedirect(request, response, "/test?oauth2Id="+principal.getName());
        } else {
            log.info("회원있을때 - 홈으로");
            getRedirectStrategy().sendRedirect(request, response, "/");
        }
    }
}
