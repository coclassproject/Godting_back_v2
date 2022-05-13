package com.gts.godtingDev.config.oauth2.provider;

import java.util.Map;

public class OAuth2NaverSet implements OAuth2UserSet {

    private String access_token;

    private String refresh_token;

    private String token_type;

    private String expires_in;

    private Map<String, Object> attributes;

    public OAuth2NaverSet(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getProvider() {
        return "naver";
    }
}
