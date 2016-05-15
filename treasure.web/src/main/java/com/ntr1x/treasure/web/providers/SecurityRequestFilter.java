package com.ntr1x.treasure.web.providers;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.Priority;
import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import com.ntr1x.treasure.web.providers.UserElementFilter.SecurityUserPrincipal;

import lombok.Data;

@Provider
@Priority(Priorities.AUTHORIZATION)
public class SecurityRequestFilter implements ContainerRequestFilter {
	
	@Context
	private HttpServletRequest request;
	
	@Inject
	private BeanManager beans;
	
	@Override
	public void filter(ContainerRequestContext rc) {
		
		Bean<?> bean = (Bean<?>) beans.getBeans(SecurityContextInstance.class).iterator().next();
		CreationalContext<?> cctx = beans.createCreationalContext(bean);
		SecurityContextInstance context = (SecurityContextInstance) beans.getReference(bean, SecurityContextInstance.class, cctx);
		
		context.setUserPrincipal((SecurityUserPrincipal) request.getAttribute(SecurityUserPrincipal.class.getName()));
		context.setUriInfo(new UriInfoState(rc.getUriInfo()));
		context.setSecure(true);
		
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
	
	@Data
	public static class SecurityContextInstance implements SecurityContext {
		
		private SecurityUserPrincipal userPrincipal;
		private UriInfoState uriInfo;
		private boolean secure;
		
		@PersistenceContext(unitName="treasure.core")
		private EntityManager em;

		@Override
		@Transactional
		public boolean isUserInRole(String role) {
			
			if (userPrincipal == null
				|| userPrincipal.session == null
				|| userPrincipal.session.getUser() == null
				|| userPrincipal.session.getUser().isLocked()
				|| !userPrincipal.session.getUser().isConfirmed()
			) return false;
			
			switch (role) {
				case "auth": return true;
				case "admin": return userPrincipal.session.getUser().isAdmin();
			}
			
			if (role.startsWith("res://")) {
				
				
				String name = MessageFormat.format(role, uriInfo.params).substring("res://".length());
				
				int pos = name.indexOf(':');
				if (pos >= 0) {
					
					String resource = name.substring(0, pos);
					String action = name.substring(pos + 1);
					
					int count = em.createNamedQuery("SecurityUserEntity", Integer.class)
							.setParameter("action", action)
							.setParameter("resource", resource)
							.getSingleResult();
					
					return count > 0;
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