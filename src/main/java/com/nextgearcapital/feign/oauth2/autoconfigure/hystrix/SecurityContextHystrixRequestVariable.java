package com.nextgearcapital.feign.oauth2.autoconfigure.hystrix;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import org.springframework.security.core.context.SecurityContext;

/**
 * Holder for the HystrixRequestVariableDefault instance that contains the SecurityContext.
 */
public class SecurityContextHystrixRequestVariable {

    private static final HystrixRequestVariableDefault<SecurityContext> securityContextVariable = new HystrixRequestVariableDefault<SecurityContext>();

    private SecurityContextHystrixRequestVariable() {}

    public static HystrixRequestVariableDefault<SecurityContext> getInstance() {
        return securityContextVariable;
    }

}