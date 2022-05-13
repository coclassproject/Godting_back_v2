package com.gts.godtingDev.config.oauth2;

import com.gts.godtingDev.config.oauth2.provider.OAuth2GoogleSet;
import com.gts.godtingDev.config.oauth2.provider.OAuth2KakaoSet;
import com.gts.godtingDev.config.oauth2.provider.OAuth2UserSet;
import com.gts.godtingDev.user.User;
import com.gts.godtingDev.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 유저 정보 받아오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        OAuth2UserSet oAuth2UserSet;

        // 로그인 플랫폼 확인
        String flatForm = userRequest.getClientRegistration().getRegistrationId();

        // 인증 세션 및 유저 정보 받아오기
        oAuth2UserSet = classifiedByFlatform(oAuth2User, flatForm);
        User user = getUser(oAuth2UserSet);

        return new PrincipalDetails(user, oAuth2UserSet);
    }

    private User getUser(OAuth2UserSet oAuth2UserSet) {
        String oAuth2Id = oAuth2UserSet.getProvider() + oAuth2UserSet.getProviderId();
        User user = userRepository.findByOAuth2Id(oAuth2Id);

        return user;
    }

    private OAuth2UserSet classifiedByFlatform(OAuth2User oAuth2User, String flatForm) {
        if (flatForm.equals("kakao")) {
            return new OAuth2KakaoSet(oAuth2User.getAttributes());
        } else if (flatForm.equals("naver")) {
            return new OAuth2KakaoSet(oAuth2User.getAttributes());
        } else if (flatForm.equals("google")) {
            return new OAuth2GoogleSet(oAuth2User.getAttributes());
        } else {
            // throw new CustomException(ExceptionMessage.OAUTH2_PROVIDER_NOT_FOUND);
            return null;
        }
    }

}
