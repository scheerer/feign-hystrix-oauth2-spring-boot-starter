package com.nextgearcapital.feign.oauth2.autoconfigure.hystrix;

import com.netflix.hystrix.HystrixCommand;
import feign.hystrix.HystrixFeign;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import javax.servlet.Filter;

/**
 * Provides request filters to pass along current request ThreadLocal Spring SecurityContext into HystrixRequestContext threads
 * so that we can access the SecurityContext inside Hystrix executed code.
 *
 * @author russell.scheerer
 */
@Configuration
@ConditionalOnClass({ HystrixCommand.class, HystrixFeign.class })
@ConditionalOnBean(name = AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, value = Filter.class)
@ConditionalOnProperty(name = "feign.hystrix.enabled", matchIfMissing = true)
public class HystrixSecurityContextAutoConfiguration {

    @Bean
    public FilterRegistrationBean securityFilterChain(@Qualifier(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME) Filter securityFilter) {
        FilterRegistrationBean registration = new FilterRegistrationBean(securityFilter);
        registration.setOrder(Integer.MAX_VALUE - 2);
        registration.setName(AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME);
        return registration;
    }

    @Bean
    public FilterRegistrationBean hystrixRequestContextEnablerFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        HystrixRequestContextEnablerFilter userFilter = new HystrixRequestContextEnablerFilter();
        registrationBean.setFilter(userFilter);
        registrationBean.setOrder(Integer.MAX_VALUE - 1);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean securityContextHystrixRequestVariableSetterFilterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        SecurityContextHystrixRequestVariableSetterFilter userFilter = new SecurityContextHystrixRequestVariableSetterFilter();
        registrationBean.setFilter(userFilter);
        registrationBean.setOrder(Integer.MAX_VALUE);
        return registrationBean;
    }
}
