package com.gts.godting.config.oauth2;

import com.gts.godting.config.exception.CustomException;
import com.gts.godting.config.exception.ExceptionMessage;
import com.gts.godting.config.oauth2.provider.GoogleUserInfo;
import com.gts.godting.config.oauth2.provider.KakaoUserInfo;
import com.gts.godting.config.oauth2.provider.NaverUserInfo;
import com.gts.godting.config.oauth2.provider.OAuth2UserInfo;
import com.gts.godting.user.User;
import com.gts.godting.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserInfo oAuth2UserInfo = null;

        String flatForm = userRequest.getClientRegistration().getRegistrationId();
        oAuth2UserInfo = classifiedByFlatForm(oAuth2User, flatForm);

        User user = getUser(oAuth2UserInfo);

        return new PrincipalDetails(user, oAuth2UserInfo);
    }

    private User getUser(OAuth2UserInfo oAuth2UserInfo) {
        String provider = oAuth2UserInfo.getProvider();
        String provider_id = oAuth2UserInfo.getProviderId();
        String oauthId = provider + "_" + provider_id;
        User user = userRepository.findByOauth2Id(oauthId);

        return user;
    }

    private OAuth2UserInfo classifiedByFlatForm(OAuth2User oAuth2User, String flatForm) {
        OAuth2UserInfo oAuth2UserInfo;
        if (flatForm.equals("google")) {
            return new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (flatForm.equals("naver")) {
            return new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else if (flatForm.equals("kakao")) {
            return new KakaoUserInfo(oAuth2User.getAttributes());
        }else{
            throw new CustomException(ExceptionMessage.OAUTH2_PROVIDER_NOT_FOUND);
        }
    }
}
