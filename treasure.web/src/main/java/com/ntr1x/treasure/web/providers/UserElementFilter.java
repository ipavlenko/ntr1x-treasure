package com.ntr1x.treasure.web.providers;

import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.security.Principal;

import javax.annotation.Priority;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Qualifier;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.ext.Provider;

import com.ntr1x.treasure.model.security.SecurityPortal;
import com.ntr1x.treasure.model.security.SecuritySession;
import com.ntr1x.treasure.model.security.SecurityUser;
import com.ntr1x.treasure.security.ISecurityService;
import com.ntr1x.treasure.security.SecurityService;

import lombok.Data;

@Provider
@PreMatching
@Priority(Priorities.AUTHORIZATION)
public class UserElementFilter implements ContainerRequestFilter {
	
	@PersistenceContext(unitName="treasure.core")
	private EntityManager em;
	
	@Context
	private HttpServletRequest request;
	
	@Inject
	private SecurityService security;
	
	@Override
	public void filter(ContainerRequestContext rc) { 
		
		SecurityUserPrincipal p  = null;
		
		SecurityPortal portal = em.createNamedQuery("SecurityPortal.byName", SecurityPortal.class)
			.setParameter("name", rc.getUriInfo().getBaseUri().getHost())
			.getSingleResult()
		;
		
		try {
			
			Cookie cookie = rc.getCookies().get(portal.getCookieName());
			String value = cookie != null
				? cookie.getValue()
				: null
			;
			
			ISecurityService.SecuritySession session = security.parseSession(value);
			
			SecuritySession entity = em.find(SecuritySession.class, session.id);
			if (entity != null && entity.getSignature() == session.signature) {
				
				SecurityUser user = entity.getUser();
				if (user.isConfirmed() && !user.isLocked()) {
					
					p = new SecurityUserPrincipal(entity, portal);
				}
			}
			
		} catch (Exception e) {
			// ignore
		}
		
		if (p == null) {
			p = new SecurityUserPrincipal(null, portal);
		}
		
		request.setAttribute(SecurityUserPrincipal.class.getName(), p);
	}
	
	@Data
	public static class SecurityUserPrincipal implements Principal, Serializable {

		private static final long serialVersionUID = -3538893803387492891L;
		
		public final SecuritySession session;
		public final SecurityPortal portal;
		
		@Override
		public String getName() {
			return session != null
				? String.format("%s", session.getUser().getEmail())
				: "Nobody"
			;
		}
		
	}
	
	@Qualifier
	@Retention(RetentionPolicy.RUNTIME)
	@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
	public @interface RequestAttribute {}
	
	@RequestScoped
	public static class SecuritySessionFactory {
		
		@Inject
		private HttpServletRequest request;
		
		@Produces @RequestAttribute
		public SecuritySession produce() {
			
			SecurityUserPrincipal principal = (SecurityUserPrincipal) request.getAttribute(SecurityUserPrincipal.class.getName());
			return principal == null ? null : principal.session;
		}
	}
	
	@RequestScoped
	public static class SecurityPortalFactory {
		
		@Inject
		private HttpServletRequest request;
		
		@Produces @RequestAttribute
		public SecurityPortal produce() {
			
			SecurityUserPrincipal principal = (SecurityUserPrincipal) request.getAttribute(SecurityUserPrincipal.class.getName());
			return principal == null ? null : principal.portal;
		}
	}
}