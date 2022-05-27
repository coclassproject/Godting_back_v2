package com.gts.godtingDev.config.oauth2.filter;

import com.gts.godtingDev.config.exception.CustomException;
import com.gts.godtingDev.config.exception.ExceptionMessage;
import com.gts.godtingDev.config.oauth2.provider.AccessTokenAuthenticationProvider;
import com.gts.godtingDev.config.oauth2.authentication.AccessTokenSocialTypeToken;
import com.gts.godtingDev.user.oauth2.SocialType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class OAuth2AccessTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX = "/login/oauth2/";
    private static final String HTTP_METHOD = "GET";
    private static final String ACCESS_TOKEN_HEADER_NAME = "Authorization";

    private static final AntPathRequestMatcher DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher(DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX + "*", HTTP_METHOD);

    public OAuth2AccessTokenAuthenticationFilter(AccessTokenAuthenticationProvider accessTokenAuthenticationProvider,
                                                 AuthenticationSuccessHandler authenticationSuccessHandler,
                                                 AuthenticationFailureHandler authenticationFailureHandler) {
        super(DEFAULT_OAUTH2_LOGIN_PATH_REQUEST_MATCHER);

        // AbstractAuthenticationProessingFilter 커스텀 위한 ProviderManager 지정
        this.setAuthenticationManager(new ProviderManager(accessTokenAuthenticationProvider));
        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        // AbstractAuthenticationProcessingFilter 구현, Authentication 반환

        SocialType socialType = extractSocialType(request);

        String accessToken = request.getHeader(ACCESS_TOKEN_HEADER_NAME);
        log.info("{} Authentication 진행중", socialType.getSocialName());

        return this.getAuthenticationManager().authenticate(new AccessTokenSocialTypeToken(accessToken, socialType));
    }

    public SocialType extractSocialType(HttpServletRequest request) {
        return Arrays.stream(SocialType.values())
                .filter(socialType ->
                        socialType.getSocialName()
                                .equals(request.getRequestURI().substring(DEFAULT_OAUTH2_LOGIN_REQUEST_URL_PREFIX.length())))
                .findFirst()
                .orElseThrow(() -> new CustomException(ExceptionMessage.URL_NOT_MATCHED));
    }
}
