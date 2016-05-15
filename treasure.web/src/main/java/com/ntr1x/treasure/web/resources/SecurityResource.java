//package com.ntr1x.treasure.web.resources;
//
//import javax.enterprise.context.RequestScoped;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.transaction.Transactional;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.GET;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.MediaType;
//
//import com.ntr1x.treasure.model.security.SecurityPortal;
//import com.ntr1x.treasure.model.security.SecurityUser;
//import com.ntr1x.treasure.web.providers.UserElementFilter.RequestAttribute;
//
//import io.swagger.annotations.Api;
//
//@Api("Security")
//@RequestScoped
//@Path("/ws/security")
//public class SecurityResource {
//
//	@PersistenceContext(unitName="treasure.core")
//	private EntityManager em;
//
//	@Inject @RequestAttribute
//	private SecurityPortal portal;
//
//	@GET
//	@Path("/users")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public SecurityUser[] usersQuery(
//			@QueryParam("confirmed") Boolean confirmed,
//			@QueryParam("locked") Boolean locked,
//			@QueryParam("admin") Boolean admin
//	) {
//
//		return em.createNamedQuery("SecurityUser.accessibleFiltered", SecurityUser.class)
//			.setParameter("portal", portal.getId())
//			.setParameter("confirmed", confirmed)
//			.setParameter("locked", locked)
//			.setParameter("admin", admin)
//			.getResultList()
//			.stream()
//			.map((entity) -> {
//				em.detach(entity);
//				entity.setPwdhash(null);
//				return entity;
//			})
//			.toArray(SecurityUser[]::new)
//		;
//	}
//
//	@GET
//	@Path("/users/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public SecurityUser usersRead(
//		@PathParam("id") long id
//	) {
//
//		SecurityUser user = em.createNamedQuery("SecurityUser.accessibleById", SecurityUser.class)
//			.setParameter("portal", portal.getId())
//			.setParameter("id", id)
//			.getSingleResult();
//
//		em.detach(user);
//		user.setPwdhash(null);
//
//		return user;
//	}
//
//	@GET
//	@Path("/users/i/{id}/lock")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public SecurityUser usersLock(
//		@PathParam("id") long id
//	) {
//
//		SecurityUser user = em.createNamedQuery("SecurityUser.accessibleById", SecurityUser.class)
//			.setParameter("portal", portal.getId())
//			.setParameter("id", id)
//			.getSingleResult();
//
//		user.setLocked(true);
//
//		em.flush();
//
//		em.detach(user);
//		user.setPwdhash(null);
//
//		return user;
//	}
//
//	@GET
//	@Path("/users/i/{id}/unlock")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public SecurityUser usersUnlock(
//		@PathParam("id") long id
//	) {
//
//		SecurityUser user = em.createNamedQuery("SecurityUser.accessibleById", SecurityUser.class)
//			.setParameter("portal", portal.getId())
//			.setParameter("id", id)
//			.getSingleResult();
//
//		user.setLocked(false);
//
//		em.flush();
//
//		em.detach(user);
//		user.setPwdhash(null);
//
//		return user;
//	}
//
//	@DELETE
//	@Path("/users/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public void usersDelete(
//		@PathParam("id") long id
//	) {
//
//		SecurityUser entity = em.createNamedQuery("SecurityUser.accessibleById", SecurityUser.class)
//			.setParameter("portal", portal.getId())
//			.setParameter("id", id)
//			.getSingleResult();
//
//		em.remove(entity);
//	}
//}
