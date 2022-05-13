package com.gts.godtingDev.config.oauth2;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        if(principalDetails.getUser() == null) {
            getRedirectStrategy().sendRedirect(request, response, "");
        } else {
            // TODO : Request 들어오는 값 Response 실어 로그인 페이지로 넘기기
            getRedirectStrategy().sendRedirect(request, response, "/");
        }
    }
}
