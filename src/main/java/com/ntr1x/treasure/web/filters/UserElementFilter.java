//package com.ntr1x.treasure.web.filters;
//
//import java.io.Serializable;
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//import java.security.Principal;
//
//import javax.annotation.Priority;
//import javax.enterprise.inject.Produces;
//import javax.inject.Inject;
//import javax.inject.Qualifier;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.servlet.http.HttpServletRequest;
//import javax.ws.rs.Priorities;
//import javax.ws.rs.container.ContainerRequestContext;
//import javax.ws.rs.container.ContainerRequestFilter;
//import javax.ws.rs.container.PreMatching;
//import javax.ws.rs.core.Context;
//import javax.ws.rs.ext.Provider;
//
//import lombok.Data;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Provider
//@PreMatching
//@Priority(Priorities.AUTHORIZATION)
//public class UserElementFilter implements ContainerRequestFilter {
//
//	@PersistenceContext
//	private EntityManager em;
//
//	@Context
//	private HttpServletRequest request;
//
//	@Inject
//	private SecurityService security;
//
//	@Override
//	public void filter(ContainerRequestContext rc) { 
//
//		SecurityUserPrincipal p  = null;
//
//		try {
//
//			String value = rc.getHeaderString("Authorization");				// Вместо cookies для авторизации используем Header
////			Cookie cookie = rc.getCookies().get(SecuritySession.COOKIE);
////			String value = cookie != null
////				? cookie.getValue()
////				: null
////			;
//
//			if (value != null){
//				SecuritySession session = security.parseSession(value);
//
//				SecuritySession entity = em.find(SecuritySession.class, session.getId());
//				if (entity != null && entity.getSignature() == session.getSignature()) {
//
//					SecurityUser user = entity.getUser();
//					if (user.isConfirmed() && !user.isLocked()) {
//
//						p = new SecurityUserPrincipal(entity);
//					}
//				}
//			}
//
//		} catch (Exception e) {
//			log.error("{}", e);
//			// ignore
//		}
//
//		if (p == null) {
//			p = new SecurityUserPrincipal(null);
//		}
//
//		request.setAttribute(SecurityUserPrincipal.class.getName(), p);
//	}
//
//	@Data
//	public static class SecurityUserPrincipal implements Principal, Serializable {
//
//		private static final long serialVersionUID = -3538893803387492891L;
//
//		public final SecuritySession session;
//
//		@Override
//		public String getName() {
//			return session != null
//				? String.format("%s", session.getUser().getEmail())
//				: "Nobody"
//			;
//		}
//	}
//
//	@Qualifier
//	@Retention(RetentionPolicy.RUNTIME)
//	@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER })
//	public @interface RequestAttribute {}
//
////	@RequestScoped
//	public static class SecuritySessionFactory {
//
//		@Inject
//		private HttpServletRequest request;
//
//		@Produces
//		@RequestAttribute
//		public SecuritySession produce() {
//
//			SecurityUserPrincipal principal = (SecurityUserPrincipal) request.getAttribute(SecurityUserPrincipal.class.getName());
//			return principal == null ? null : principal.session;
//		}
//	}
//
////	@RequestScoped
//	public static class SecurityUserFactory {
//
//		@Inject
//		private HttpServletRequest request;
//
//		@Produces
//		@RequestAttribute
//		public SecurityUser produce() {
//
//			SecurityUserPrincipal principal = (SecurityUserPrincipal) request.getAttribute(SecurityUserPrincipal.class.getName());
//			return principal == null ? null : principal.session.getUser();
//		}
//	}
//}