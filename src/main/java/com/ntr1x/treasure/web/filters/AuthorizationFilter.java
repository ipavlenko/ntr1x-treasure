package com.ntr1x.treasure.web.filters;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.filters.AuthenticationFilter.AccountPrincipal;
import com.ntr1x.treasure.web.services.ISecurityService;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthorizationFilter implements ContainerRequestFilter {
	
	@Inject
	private HttpServletRequest request;
	
	@Inject
	private ISecurityService security;
	
	@Override
	public void filter(ContainerRequestContext rc) {
		
	    SecurityContextInstance context = new SecurityContextInstance(
            security,
            (AccountPrincipal) request.getAttribute(AccountPrincipal.class.getName()),
            new UriInfoState(rc.getUriInfo()),
            true
	    );
		
		rc.setSecurityContext(context);
	}
	
	/**
	 * UriInfo is mutable, UriInfoState stores relevant UriInfo
	 */
	private static class UriInfoState {
		
		private final Map<String, Object> params = new HashMap<String, Object>();
		
		public UriInfoState(UriInfo info) {
				
			for (Entry<String, List<String>> entry : info.getPathParameters().entrySet()) {
				params.put(entry.getKey(), entry.getValue().get(0));
			}
		}
	}
	
	@Getter
	@RequiredArgsConstructor
	public static class SecurityContextInstance implements SecurityContext {
		
	    private final ISecurityService security;
		private final AccountPrincipal userPrincipal;
		private final UriInfoState uriInfo;
		private final boolean secure;
		
		@Override
		@Transactional
		public boolean isUserInRole(String role) {
			
			if (userPrincipal == null
				|| userPrincipal.session == null
				|| userPrincipal.session.getAccount() == null
//				|| principal.session.getAccount().isLocked()
//				|| !principal.session.getAccount().isConfirmed()
			) return false;
			
			switch (role) {
				case "auth": return true;
//				case "admin": return principal.session.getAccount().isAdmin();
			}
			
			if (role.startsWith("res://")) {
				
				String name = MessageFormat.format(role, uriInfo.params).substring("res://".length());
				
				int pos = name.indexOf(':');
				if (pos >= 0) {
					
					String resource = name.substring(0, pos);
					String action = name.substring(pos + 1);

					return security.isUserInRole(userPrincipal.getSession().getAccount(), resource, action);
				}
				
				return false;
			}
			
			return false;
		}

		@Override
		public String getAuthenticationScheme() {
			return SecurityContext.FORM_AUTH;
		}
	}
}