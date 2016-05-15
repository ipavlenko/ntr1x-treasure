//package com.ntr1x.treasure.web.resources;
//
//import java.util.List;
//
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.transaction.Transactional;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.FormParam;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//
//import com.ntr1x.treasure.model.security.SecurityPortal;
//import com.ntr1x.treasure.model.security.SecurityProvider;
//import com.ntr1x.treasure.web.providers.UserElementFilter.RequestAttribute;
//
//import io.swagger.annotations.Api;
//
//@Api("OAuthProviders")
//@Path("/ws/oauth")
//public class OAuthResource {
//
//	@PersistenceContext(unitName="treasure.core")
//	private EntityManager em;
//
//	@Inject @RequestAttribute
//	private SecurityPortal portal;
//
//	@GET
//	@Path("/providers")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public List<SecurityProvider> providersQuery(
//	) {
//
//		return em
//			.createNamedQuery("SecurityProvider.accessible", SecurityProvider.class)
//			.setParameter("portal", portal.getId())
//			.getResultList()
//		;
//	}
//
//	@GET
//	@Path("/providers/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public SecurityProvider providersRead(
//		@PathParam("id") long id
//	) {
//
//		return em.createNamedQuery("SecurityProvider.accessibleById", SecurityProvider.class)
//			.setParameter("portal", portal.getId())
//			.setParameter("id", id)
//			.getSingleResult();
//	}
//
//	@POST
//	@Path("/providers")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public SecurityProvider providersCreate(
//			@FormParam("name") String name,
//			@FormParam("clientId") String clientId,
//			@FormParam("clientSecret") String clientSecret,
//			@FormParam("redirectUri") String redirectUri
//	) {
//
//		SecurityProvider entity = new SecurityProvider(
//			0L,
//			portal,
//			name,
//			clientId,
//			clientSecret,
//			redirectUri
//		);
//
//		em.persist(entity);
//		em.flush();
//
//		return entity;
//	}
//
//	@PUT
//	@Path("/providers/i/{id}")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public SecurityProvider providersUpdate(
//			@PathParam("id") long id,
//			@FormParam("name") String name,
//			@FormParam("clientId") String clientId,
//			@FormParam("clientSecret") String clientSecret,
//			@FormParam("redirectUri") String redirectUri
//	) {
//
//		SecurityProvider entity = em.createNamedQuery("SecurityProvider.accessibleById", SecurityProvider.class)
//			.setParameter("portal", portal.getId())
//			.setParameter("id", id)
//			.getSingleResult();
//
//		entity.setName(name);
//		entity.setClientId(clientId);
//		entity.setClientSecret(clientSecret);
//		entity.setRedirectUri(redirectUri);
//
//		em.merge(entity);
//
//		return entity;
//	}
//
//	@DELETE
//	@Path("/providers/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public void providersDelete(
//		@PathParam("id") long id
//	) {
//
//		SecurityProvider entity = em.createNamedQuery("SecurityProvider.accessibleById", SecurityProvider.class)
//			.setParameter("portal", portal.getId())
//			.setParameter("id", id)
//			.getSingleResult();
//
//		em.remove(entity);
//	}
//}