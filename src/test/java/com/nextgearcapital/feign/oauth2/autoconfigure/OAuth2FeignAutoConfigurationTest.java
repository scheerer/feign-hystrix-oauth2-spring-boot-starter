package com.nextgearcapital.feign.oauth2.autoconfigure;

import org.junit.After;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link OAuth2FeignAutoConfiguration}
 *
 * @author russell.scheerer
 */
public class OAuth2FeignAutoConfigurationTest {
    private ConfigurableApplicationContext context;

    @After
    public void after() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Test
    public void oAuth2FeignRequestInterceptorIsAutoconfiguredInApplicationContexts() {
        this.context = new AnnotationConfigWebApplicationContext();
        ((AnnotationConfigWebApplicationContext) this.context)
                .register(OAuth2FeignAutoConfiguration.class);
        this.context.refresh();
//        ((AnnotationConfigWebApplicationContext) this.context)
//                .setServletContext(new MockServletContext());
        Map<String, OAuth2FeignRequestInterceptor> beans = this.context
                .getBeansOfType(OAuth2FeignRequestInterceptor.class);
        assertThat(beans).hasSize(1);
    }
}
