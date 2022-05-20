package com.gts.godtingDev.config.oauth2.handler;

import com.gts.godtingDev.config.auth.token.JwtToken;
import com.gts.godtingDev.config.auth.token.JwtTokenProvider;
import com.gts.godtingDev.config.auth.token.JwtTokenRepository;
import com.gts.godtingDev.config.auto.CookieConfig;
import com.gts.godtingDev.config.oauth2.authentication.OAuth2UserDetails;
import com.gts.godtingDev.config.redis.RedisUtil;
import com.gts.godtingDev.user.User;
import com.gts.godtingDev.user.UserRepository;
import com.gts.godtingDev.user.oauth2.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    private final JwtTokenProvider jwtTokenProvider;
    private final CookieConfig cookieConfig;
    private final JwtTokenRepository jwtTokenRepository;
    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2UserDetails principal = (OAuth2UserDetails) authentication.getPrincipal();
        JwtToken jwtToken;
        String accessToken;

        if (authentication.getAuthorities().stream().anyMatch(s -> s.getAuthority().equals(Role.GUEST.getGratedAuthority()))){
            getRedirectStrategy().sendRedirect(request, response, redirectUrl + "sign_in?social_type=" +
                    principal.getSocialType() + "&social_id=" +
                    principal.getSocialId());
        } else {
            // 엑세스 발급
            accessToken = jwtTokenProvider.accessToken(authentication);
            response.setHeader("Authorization", accessToken);

            // 리프레시 발급
            jwtToken = new JwtToken(principal.getSocialId(), jwtTokenProvider.refreshToken(authentication));
            jwtTokenRepository.save(jwtToken);
            response.addCookie(cookieConfig.addCookie("RefreshToken", jwtToken.getRefreshToken()));

            // 유저 정보 불러와 저장
            Optional<User> userEntity = userRepository.findBySocialId(principal.getSocialId());
            User user = userEntity.get();
            response.addCookie(cookieConfig.addCookie("User", user.toString()));


            // REDIRECT
            getRedirectStrategy().sendRedirect(request, response, redirectUrl);
        }
    }
}
