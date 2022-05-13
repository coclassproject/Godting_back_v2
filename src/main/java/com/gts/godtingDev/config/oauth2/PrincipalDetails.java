package com.gts.godtingDev.config.oauth2;

import com.gts.godtingDev.config.oauth2.provider.OAuth2UserSet;
import com.gts.godtingDev.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
public class PrincipalDetails implements OAuth2User {

    private User user;
    private OAuth2UserSet oAuth2UserSet;

    public PrincipalDetails(User user, OAuth2UserSet oAuth2UserSet) {
        this.user = user;
        this.oAuth2UserSet = oAuth2UserSet;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2UserSet.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        // OAuth 2.0 ID 값 리턴
        return oAuth2UserSet.getProvider() + oAuth2UserSet.getProviderId();
    }
}
