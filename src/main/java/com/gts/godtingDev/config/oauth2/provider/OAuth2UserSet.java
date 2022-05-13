package com.gts.godtingDev.config.oauth2.provider;

import java.util.Map;

public interface OAuth2UserSet {

    Map<String, Object> getAttributes();

    String getProviderId();

    String getProvider();
}
