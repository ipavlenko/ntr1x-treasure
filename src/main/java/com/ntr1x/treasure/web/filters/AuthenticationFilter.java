package com.ntr1x.treasure.web.filters;

import java.io.Serializable;
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.ext.Provider;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.security.SecuritySession;
import com.ntr1x.treasure.web.model.security.SecurityUser;
import com.ntr1x.treasure.web.services.ISecurityService;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Provider
@PreMatching
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

	@Inject
	private HttpServletRequest request;

	@Inject
	private ISecurityService security;
	
	@Inject
    private EntityManager em;
		
	@Override
	public void filter(ContainerRequestContext rc) { 

		UserPrincipal p  = null;

		try {

			String value = rc.getHeaderString("Authorization");

			if (value != null) {
			    
				ISecurityService.SecuritySession parsed = security.parseSession(value);

				SecuritySession session = em.find(SecuritySession.class, parsed.getId());
				
				if (session != null && session.getSignature() == parsed.getSignature()) {

				    SecurityUser user = session.getUser();
				    if (user.isConfirmed() && !user.isLocked()) {
				        p = new UserPrincipal(session);
				    }
				}
			}

		} catch (Exception e) {
			log.error("{}", e);
			// ignore
		}

		if (p == null) {
			p = new UserPrincipal(null);
		}

		request.setAttribute(UserPrincipal.class.getName(), p);
	}

	@Data
	public static class UserPrincipal implements Principal, Serializable {

		private static final long serialVersionUID = -3538893803387492891L;

		public final SecuritySession session;

		@Override
		public String getName() {
			return session != null
				? String.format("%s", session.getUser().getEmail())
				: "Nobody"
			;
		}
	}

	@Configuration
    public static class PrincipalFactory {

        @Inject
        private HttpServletRequest request;

        @Bean
        @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
        public UserPrincipal produce() {

            return (UserPrincipal) request.getAttribute(UserPrincipal.class.getName());
        }
    }
	
	@Configuration
	public static class SessionFactory {

		@Inject
		private HttpServletRequest request;

		@Bean
		@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
		public SecuritySession produce() {

		    UserPrincipal principal = (UserPrincipal) request.getAttribute(UserPrincipal.class.getName());
			return principal == null ? null : principal.session;
		}
	}

	@Configuration
	public static class AccountFactory {

		@Inject
		private HttpServletRequest request;

		@Bean
        @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
		public SecurityUser produce() {

			UserPrincipal principal = (UserPrincipal) request.getAttribute(UserPrincipal.class.getName());
			return principal == null ? null : principal.session.getUser();
		}
	}
}