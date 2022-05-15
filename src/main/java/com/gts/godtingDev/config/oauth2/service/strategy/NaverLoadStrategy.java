package com.gts.godtingDev.config.oauth2.service.strategy;

import com.gts.godtingDev.config.exception.CustomException;
import com.gts.godtingDev.config.exception.ExceptionMessage;
import com.gts.godtingDev.user.oauth2.SocialType;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class NaverLoadStrategy extends SocialLoadStrategy{


    protected String sendRequestToSocialSite(HttpEntity request){
        try {
            ResponseEntity<Map<String, Object>> response = restTemplate.exchange(SocialType.NAVER.getUserInfoUrl(),//
                    SocialType.NAVER.getMethod(),
                    request,
                    RESPONSE_TYPE);


            Map<String , Object> response2 = ( Map<String , Object>)response.getBody().get("response");
            return response2.get("id").toString();
        } catch (Exception e) {
            throw new CustomException(ExceptionMessage.SERVER_OAUTH2_ACCESS_TOKEN_ERROR);
        }
    }
}