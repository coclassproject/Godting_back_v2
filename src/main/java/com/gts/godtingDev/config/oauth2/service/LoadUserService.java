package com.gts.godtingDev.config.oauth2.service;

import com.gts.godtingDev.config.exception.CustomException;
import com.gts.godtingDev.config.exception.ExceptionMessage;
import com.gts.godtingDev.config.oauth2.service.strategy.GoogleLoadStrategy;
import com.gts.godtingDev.config.oauth2.service.strategy.KakaoLoadStrategy;
import com.gts.godtingDev.config.oauth2.service.strategy.NaverLoadStrategy;
import com.gts.godtingDev.config.oauth2.service.strategy.SocialLoadStrategy;
import com.gts.godtingDev.config.oauth2.authentication.AccessTokenSocialTypeToken;
import com.gts.godtingDev.config.oauth2.authentication.OAuth2UserDetails;
import com.gts.godtingDev.user.oauth2.SocialType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class LoadUserService {

    private final RestTemplate restTemplate = new RestTemplate();


    public OAuth2UserDetails getOAuth2UserDetails(AccessTokenSocialTypeToken authentication)  {

        SocialType socialType = authentication.getSocialType();

        SocialLoadStrategy socialLoadStrategy = getSocialLoadStrategy(socialType); // SocialLoadStrategy 설정

        String socialPk = socialLoadStrategy.getSocialPk(authentication.getAccessToken()); // PK 가져오기

        return OAuth2UserDetails.builder() // PK와 SocialType을 통해 회원 생성
                .socialId(socialPk)
                .socialType(socialType)
                .build();
    }

    private SocialLoadStrategy getSocialLoadStrategy(SocialType socialType) {
        return switch (socialType){
            case KAKAO -> new KakaoLoadStrategy();
            case GOOGLE ->  new GoogleLoadStrategy();
            case NAVER ->  new NaverLoadStrategy();
            default -> throw new CustomException(ExceptionMessage.NOT_SUPPORTED_LOGIN_PROCESS);
        };
    }


}