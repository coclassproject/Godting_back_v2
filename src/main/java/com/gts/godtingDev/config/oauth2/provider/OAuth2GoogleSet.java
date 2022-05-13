package com.gts.godtingDev.config.oauth2.provider;

import java.util.Map;

public class OAuth2GoogleSet implements OAuth2UserSet {



    private Map<String, Object> attributes;
    public OAuth2GoogleSet(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    // TODO : 구글 구현 필요

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getProviderId() {
        return null;
    }

    @Override
    public String getProvider() {
        return null;
    }
}
