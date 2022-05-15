package com.gts.godtingDev.config.oauth2.service.strategy;

import com.gts.godtingDev.config.exception.CustomException;
import com.gts.godtingDev.config.exception.ExceptionMessage;
import com.gts.godtingDev.user.oauth2.SocialType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class KakaoLoadStrategy extends SocialLoadStrategy{



    protected String sendRequestToSocialSite(HttpEntity request){
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(SocialType.KAKAO.getUserInfoUrl(),// -> /v2/user/me
                    SocialType.KAKAO.getMethod(),
                    request,
                    RESPONSE_TYPE);

            return response.getBody().get("id").toString(); // 카카오는 id를 PK로 사용

        } catch (Exception e) {
            throw new CustomException(ExceptionMessage.SERVER_OAUTH2_ACCESS_TOKEN_ERROR);
        }
    }
}