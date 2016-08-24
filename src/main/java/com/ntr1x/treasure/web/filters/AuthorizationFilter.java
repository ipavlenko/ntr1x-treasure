package com.ntr1x.treasure.web.filters;

import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.services.ISecurityContextService;
import com.ntr1x.treasure.web.services.ISecurityContextService.UriInfoState;

import lombok.RequiredArgsConstructor;

@Component
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationFilter implements ContainerRequestFilter {
	
	@Inject
	private ISecurityContextService security;
	
	@Override
	public void filter(ContainerRequestContext rc) {
		
	    rc.setSecurityContext(
		    new SecurityContextInstance(
		        security,
		        new UriInfoState(rc.getUriInfo())
		    )
		);
	}
	
	@RequiredArgsConstructor
	public static class SecurityContextInstance implements SecurityContext {
		
	    private final ISecurityContextService security;
	    private final ISecurityContextService.UriInfoState state;
		
		@Override
		@Transactional
		public boolean isUserInRole(String role) {
			return security.isUserInRole(state, role);
		}

        @Override
		public Principal getUserPrincipal() {
		    return security.getUserPrincipal();
		}

		@Override
		public String getAuthenticationScheme() {
			return SecurityContext.FORM_AUTH;
		}
		
		@Override
		public boolean isSecure() {
		    return true;
		}
	}
}