package com.ntr1x.treasure.web.filters;

import java.io.Serializable;
import java.security.Principal;

import javax.annotation.Priority;
import javax.inject.Inject;
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

import com.ntr1x.treasure.web.model.Account;
import com.ntr1x.treasure.web.model.Session;
import com.ntr1x.treasure.web.repository.SessionRepository;
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
    private SessionRepository sessions;
	
	@Override
	public void filter(ContainerRequestContext rc) { 

		AccountPrincipal p  = null;

		try {

			String value = rc.getHeaderString("Authorization");

			if (value != null){
			    
				ISecurityService.SecuritySession parsed = security.parseSession(value);

				Session session = sessions.findByIdAndSignature(parsed.getId(), parsed.getSignature());
				if (session != null) {

				    p = new AccountPrincipal(session);
//					Account account = entity.getAccount();
//					if (account.isConfirmed() && !account.isLocked()) {
//						p = new SecurityUserPrincipal(entity);
//					}
					
				}
			}

		} catch (Exception e) {
			log.error("{}", e);
			// ignore
		}

		if (p == null) {
			p = new AccountPrincipal(null);
		}

		request.setAttribute(AccountPrincipal.class.getName(), p);
	}

	@Data
	public static class AccountPrincipal implements Principal, Serializable {

		private static final long serialVersionUID = -3538893803387492891L;

		public final Session session;

		@Override
		public String getName() {
			return session != null
				? String.format("%s", session.getAccount().getEmail())
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
        public AccountPrincipal produce() {

            return (AccountPrincipal) request.getAttribute(AccountPrincipal.class.getName());
        }
    }
	
	@Configuration
	public static class SessionFactory {

		@Inject
		private HttpServletRequest request;

		@Bean
		@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
		public Session produce() {

			AccountPrincipal principal = (AccountPrincipal) request.getAttribute(AccountPrincipal.class.getName());
			return principal == null ? null : principal.session;
		}
	}

	@Configuration
	public static class AccountFactory {

		@Inject
		private HttpServletRequest request;

		@Bean
        @Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
		public Account produce() {

			AccountPrincipal principal = (AccountPrincipal) request.getAttribute(AccountPrincipal.class.getName());
			return principal == null ? null : principal.session.getAccount();
		}
	}
}