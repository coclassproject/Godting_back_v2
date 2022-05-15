package com.gts.godtingDev.config.oauth2.handler;

import com.gts.godtingDev.config.oauth2.authentication.OAuth2UserDetails;
import com.gts.godtingDev.user.User;
import com.gts.godtingDev.user.UserRepository;
import com.gts.godtingDev.user.oauth2.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Value("${front.redirect.url}")
    private String redirectUrl;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2UserDetails principal = (OAuth2UserDetails) authentication.getPrincipal();

        if (authentication.getAuthorities().stream().anyMatch(s -> s.getAuthority().equals(Role.GUEST.getGratedAuthority()))){
            getRedirectStrategy().sendRedirect(request, response, redirectUrl + "sign_in?social_type=" +
                    principal.getSocialType() + "&social_id=" +
                    principal.getSocialId());
        } else {
            Optional<User> user = userRepository.findBySocialTypeAndSocialId(principal.getSocialType(), principal.getSocialId());
            // 토큰 생성 후 둘다 response에 담아서 보내기
            // authentication에 토큰 생성 담기.
        }
    }
}
