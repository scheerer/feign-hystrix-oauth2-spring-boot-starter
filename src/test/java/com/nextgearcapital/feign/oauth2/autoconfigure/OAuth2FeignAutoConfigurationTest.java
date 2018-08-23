package com.nextgearcapital.feign.oauth2.autoconfigure;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.util.EnvironmentTestUtils;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OAuth2FeignAutoConfiguration}
 *
 * @author russell.scheerer
 */
public class OAuth2FeignAutoConfigurationTest {
    private AnnotationConfigApplicationContext context;

    @Before
    public void setup() {
        this.context = new AnnotationConfigApplicationContext();
    }

    @After
    public void close() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void oAuth2FeignRequestInterceptorIsAutoconfiguredInApplicationContexts() {
        load("feign.oauth2.passthrough.enabled:true");

        Map<String, OAuth2FeignRequestInterceptor> beans = this.context
                .getBeansOfType(OAuth2FeignRequestInterceptor.class);
        assertThat(beans).hasSize(1);
    }

    @Test
    public void oAuth2FeignRequestInterceptorIsAutoconfiguredInApplicationContextsWhenPropertyIsTrue() {
        load("feign.oauth2.passthrough.enabled:true");
        Map<String, OAuth2FeignRequestInterceptor> beans = this.context
                .getBeansOfType(OAuth2FeignRequestInterceptor.class);
        assertThat(beans).hasSize(1);
    }

    @Test
    public void oAuth2FeignRequestInterceptorIsNotAutoconfiguredInApplicationContextsWhenPropertyIsFalse() {
        load("feign.oauth2.passthrough.enabled:false");
        Map<String, OAuth2FeignRequestInterceptor> beans = this.context
                .getBeansOfType(OAuth2FeignRequestInterceptor.class);
        assertThat(beans).hasSize(0);
    }

    private void load(String... environment) {
        this.context = doLoad(environment);
    }

    private AnnotationConfigApplicationContext doLoad(String... environment) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        EnvironmentTestUtils.addEnvironment(applicationContext, environment);
        applicationContext.register(OAuth2FeignAutoConfiguration.class,
                PropertyPlaceholderAutoConfiguration.class);
        applicationContext.refresh();
        return applicationContext;
    }
}
