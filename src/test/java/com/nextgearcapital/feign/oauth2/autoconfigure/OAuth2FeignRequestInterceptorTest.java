package com.nextgearcapital.feign.oauth2.autoconfigure;

import feign.RequestTemplate;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * @author russell.scheerer
 */
public class OAuth2FeignRequestInterceptorTest {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TOKEN_TYPE = "Bearer";

    private OAuth2FeignRequestInterceptor oAuth2FeignRequestInterceptor = new OAuth2FeignRequestInterceptor();

    @After
    public void teardown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    public void testExistingSecurityContextBearerTokenPassesThru() throws Exception {
        RequestTemplate requestTemplate = new RequestTemplate();
        String bearerToken = "meSoSecure";

        applyOAuth2SecurityContext(bearerToken);

        oAuth2FeignRequestInterceptor.apply(requestTemplate);
        assertNotNull(requestTemplate.headers().get(AUTHORIZATION_HEADER));

        ArrayList headers = (ArrayList) requestTemplate.headers().get(AUTHORIZATION_HEADER);
        assertEquals(BEARER_TOKEN_TYPE + " " + bearerToken, headers.get(0));
    }

    @Test
    public void testNoHeaderWithNullSecurityContext() throws Exception {
        RequestTemplate requestTemplate = new RequestTemplate();

        oAuth2FeignRequestInterceptor.apply(requestTemplate);
        assertNull(requestTemplate.headers().get(AUTHORIZATION_HEADER));
    }

    @Test
    public void testExistingRequestTemplateAuthorizationHeaderOverridesSecurityContextToken() throws Exception {
        RequestTemplate requestTemplate = new RequestTemplate();
        String existingAuthHeaderValue = "meSoSecure";
        requestTemplate.header(AUTHORIZATION_HEADER, existingAuthHeaderValue);

        oAuth2FeignRequestInterceptor.apply(requestTemplate);
        assertNotNull(requestTemplate.headers().get(AUTHORIZATION_HEADER));

        ArrayList headers = (ArrayList) requestTemplate.headers().get(AUTHORIZATION_HEADER);
        assertEquals(existingAuthHeaderValue, headers.get(0));
    }

    private void applyOAuth2SecurityContext(String bearerToken) {
        OAuth2Authentication authentication = Mockito.mock(OAuth2Authentication.class);
        OAuth2AuthenticationDetails details = Mockito.mock(OAuth2AuthenticationDetails.class);

        when(authentication.getDetails()).thenReturn(details);
        when(details.getTokenValue()).thenReturn(bearerToken);
        when(details.getTokenType()).thenReturn(BEARER_TOKEN_TYPE);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
