package com.nextgearcapital.feign.oauth2.autoconfigure.hystrix;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * A HystrixCommandExecutionHook that makes the Spring SecurityContext available during the execution of HystrixCommands.
 *
 * It extracts the SecurityContext from the SecurityContextHystrixRequestVariable and sets it on the SecurityContextHolder.
 */
public class SecurityContextRegistratorCommandHook extends HystrixCommandExecutionHook {

    @Override
    public <T> void onExecutionStart(HystrixInvokable<T> commandInstance) {
        SecurityContextHolder.setContext(SecurityContextHystrixRequestVariable.getInstance().get());
    }

    /**
     * Clear the SecurityContext
     */
    @Override
    public <T> void onExecutionSuccess(HystrixInvokable<T> commandInstance) {
        SecurityContextHolder.clearContext();
    }

    /**
     * Clear the SecurityContext
     */
    @Override
    public <T> Exception onExecutionError(HystrixInvokable<T> commandInstance, Exception e) {
        SecurityContextHolder.clearContext();
        return e;
    }

}
