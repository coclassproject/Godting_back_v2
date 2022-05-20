package com.gts.godtingDev.config.oauth2.provider;

import com.gts.godtingDev.config.oauth2.service.LoadUserService;
import com.gts.godtingDev.config.oauth2.authentication.AccessTokenSocialTypeToken;
import com.gts.godtingDev.config.oauth2.authentication.OAuth2UserDetails;
import com.gts.godtingDev.user.User;
import com.gts.godtingDev.user.UserRepository;
import com.gts.godtingDev.user.oauth2.Role;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AccessTokenAuthenticationProvider implements AuthenticationProvider {
    // AuthenticationProvider 상속 받아 authenticate와 supports 구현

    private final LoadUserService loadUserService;
    private final UserRepository userRepository;



    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // ProviderManager가 호출, 인증과정 처리

        OAuth2UserDetails oAuth2User = loadUserService.getOAuth2UserDetails((AccessTokenSocialTypeToken) authentication);

        User user = saveOrGet(oAuth2User);
        oAuth2User.setRoles(user.getRole().name());

        if (user.getEmail() != null) {
            oAuth2User.setUsername(user.getEmail());
        }

        return AccessTokenSocialTypeToken.builder().principal(oAuth2User).authorities(oAuth2User.getAuthorities()).build();
    }



    private User saveOrGet(OAuth2UserDetails oAuth2User) {
        return userRepository.findBySocialTypeAndSocialId(oAuth2User.getSocialType(),
                        oAuth2User.getSocialId())
                .orElseGet(() -> userRepository.save(User.builder()
                        .socialType(oAuth2User.getSocialType())
                        .socialId(oAuth2User.getSocialId())
                        .role(Role.GUEST).build()));
    }



    @Override
    public boolean supports(Class<?> authentication) {
        return AccessTokenSocialTypeToken.class.isAssignableFrom(authentication);
    }



}