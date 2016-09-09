package com.nextgearcapital.feign.oauth2.autoconfigure;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

@Slf4j
public class OAuth2FeignRequestInterceptor implements RequestInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Override
    public void apply(RequestTemplate template) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (template.headers().containsKey(AUTHORIZATION_HEADER)) {
            log.warn("The Authorization token has been already set");
        } else if (auth == null || !(auth instanceof OAuth2Authentication)) {
            log.warn("Can not obtain existing token for request, if it is a non secured request, ignore.");
        } else {
            OAuth2Authentication auth2Authentication = (OAuth2Authentication) auth;
            OAuth2AuthenticationDetails oAuth2AuthenticationDetails = (OAuth2AuthenticationDetails) auth2Authentication.getDetails();

            log.debug("Constructing Header {} for Token {}", AUTHORIZATION_HEADER, oAuth2AuthenticationDetails.getTokenType());
            template.header(AUTHORIZATION_HEADER, String.format("%s %s", oAuth2AuthenticationDetails.getTokenType(),
                    oAuth2AuthenticationDetails.getTokenValue()));
        }
    }
}
