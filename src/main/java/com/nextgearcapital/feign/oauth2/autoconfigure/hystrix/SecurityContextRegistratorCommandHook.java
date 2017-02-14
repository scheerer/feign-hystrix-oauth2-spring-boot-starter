package com.nextgearcapital.feign.oauth2.autoconfigure.hystrix;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * A HystrixCommandExecutionHook that makes the Spring SecurityContext available during the execution of HystrixCommands.
 * <p>
 * It extracts the SecurityContext from the SecurityContextHystrixRequestVariable and sets it on the SecurityContextHolder.
 */
public class SecurityContextRegistratorCommandHook extends HystrixCommandExecutionHook {

	private static final String SECURITY_CONTEXT_SET_BY_SECURITY_CONTEXT_REGISTRATOR_COMMAND_HOOK = "securityContextSetBySecurityContextRegistratorCommandHook";

	@Override
	public <T> void onExecutionStart(HystrixInvokable<T> commandInstance) {
		if (SecurityContextHolder.getContext() == null) {
			SecurityContext securityContext = SecurityContextHystrixRequestVariable.getInstance().get();
			if (securityContext != null) {
				SecurityContextHolder.setContext(securityContext);
				RequestContextHolder.getRequestAttributes()
						.setAttribute(SECURITY_CONTEXT_SET_BY_SECURITY_CONTEXT_REGISTRATOR_COMMAND_HOOK, true, RequestAttributes.SCOPE_REQUEST);
			}
		}
	}

	/**
	 * Clear the SecurityContext
	 */
	@Override
	public <T> void onExecutionSuccess(HystrixInvokable<T> commandInstance) {
		if (RequestContextHolder.getRequestAttributes()
				.getAttribute(SECURITY_CONTEXT_SET_BY_SECURITY_CONTEXT_REGISTRATOR_COMMAND_HOOK, RequestAttributes.SCOPE_REQUEST) != null) {
			SecurityContextHolder.clearContext();
		}
	}

	/**
	 * Clear the SecurityContext
	 */
	@Override
	public <T> Exception onExecutionError(HystrixInvokable<T> commandInstance, Exception e) {
		if (RequestContextHolder.getRequestAttributes()
				.getAttribute(SECURITY_CONTEXT_SET_BY_SECURITY_CONTEXT_REGISTRATOR_COMMAND_HOOK, RequestAttributes.SCOPE_REQUEST) != null) {
			SecurityContextHolder.clearContext();
		}
		return e;
	}

}
