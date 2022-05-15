package com.gts.godtingDev.config.oauth2.authentication;

import com.gts.godtingDev.user.oauth2.SocialType;
import lombok.Builder;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AccessTokenSocialTypeToken extends AbstractAuthenticationToken {

    private Object principal; // OAuth2UserDetails 타입

    private String accessToken;
    private SocialType socialType;



    public AccessTokenSocialTypeToken(String accessToken, SocialType socialType) {
        super(null);
        this.accessToken = accessToken;
        this.socialType = socialType;
        setAuthenticated(false);
    }


    @Builder
    public AccessTokenSocialTypeToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }




    public String getAccessToken() {
        return accessToken;
    }

    public SocialType getSocialType() {
        return socialType;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }



    @Override
    public Object getCredentials() {
        return null;
    }


}