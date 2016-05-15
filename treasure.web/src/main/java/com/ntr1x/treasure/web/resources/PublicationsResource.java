//package com.ntr1x.treasure.web.resources;
//
//import java.time.LocalDate;
//import java.util.List;
//
//import javax.enterprise.context.RequestScoped;
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
//import com.ntr1x.treasure.model.content.PublicationCategoryEntity;
//import com.ntr1x.treasure.model.content.PublicationEntryEntity;
//import com.ntr1x.treasure.model.security.SecurityPortal;
//import com.ntr1x.treasure.web.providers.UserElementFilter.RequestAttribute;
//
//import io.swagger.annotations.Api;
//
//@Api("Publications")
//@RequestScoped
//@Path("/ws/content/publications")
//public class PublicationsResource {
//
//	@PersistenceContext(unitName="treasure.core")
//	private EntityManager em;
//
//	@Inject @RequestAttribute
//	private SecurityPortal portal;
//
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public List<PublicationCategoryEntity> categoriesQuery() {
//
//		return em
//			.createNamedQuery("PublicationCategoryEntity.accessible", PublicationCategoryEntity.class)
//			.setParameter("portal", portal.getName())
//			.getResultList()
//		;
//	}
//
//	@GET
//	@Path("/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public PublicationCategoryEntity categoriesReadById(
//			@PathParam("id") long id
//	) {
//
//		return em.find(PublicationCategoryEntity.class, id);
//	}
//
//	@GET
//	@Path("/n/{name}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public PublicationCategoryEntity categoriesReadByName(
//			@PathParam("name") String name
//	) {
//
//		return em
//			.createNamedQuery("PublicationCategoryEntity.accessibleByName", PublicationCategoryEntity.class)
//			.setParameter("portal", portal.getName())
//			.setParameter("name", name)
//			.getSingleResult()
//		;
//	}
//
//	@POST
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public PublicationCategoryEntity categoriesCreate(
//			@FormParam("name") String name,
//			@FormParam("title") String title
//	) {
//
//		PublicationCategoryEntity entity = new PublicationCategoryEntity(
//			0L,
//			portal,
//			name,
//			title
//		);
//
//		em.persist(entity);
//		em.flush();
//
//		return entity;
//	}
//
//	@PUT
//	@Path("/i/{id}")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public PublicationCategoryEntity categoriesUpdate(
//			@PathParam("id") long id,
//			@FormParam("name") String name,
//			@FormParam("title") String title
//	) {
//
//		PublicationCategoryEntity entity = em.find(PublicationCategoryEntity.class, id);
//
//		entity = new PublicationCategoryEntity(
//			entity.getId(),
//			portal,
//			name,
//			title
//		);
//
//		em.merge(entity);
//
//		return entity;
//	}
//
//	@DELETE
//	@Path("/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public void categoriesDelete(
//			@PathParam("id") long id
//	) {
//
//		em.remove(em.find(PublicationCategoryEntity.class, id));
//	}
//
//	@GET
//	@Path("/i/{category}/entries")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public List<PublicationEntryEntity> entriesQuery(
//			@PathParam("category") long category
//	) {
//
//		return em
//			.createNamedQuery("PublicationEntryEntity.accessible", PublicationEntryEntity.class)
//			.setParameter("portal", portal.getName())
//			.setParameter("category", category)
//			.getResultList()
//		;
//	}
//
//	@GET
//	@Path("/i/{category}/entries/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public PublicationEntryEntity entriesRead(
//			@PathParam("id") long id
//	) {
//
//		return em.find(PublicationEntryEntity.class, id);
//	}
//
//	@POST
//	@Path("/i/{category}/entries")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public PublicationEntryEntity entriesCreate(
//			@FormParam("category") long category,
//			@FormParam("title") String title,
//			@FormParam("promo") String promo,
//			@FormParam("description") String description,
//			@FormParam("date") LocalDate date
//	) {
//
//		PublicationEntryEntity entity = new PublicationEntryEntity(
//			0L,
//			portal,
//			em.find(PublicationCategoryEntity.class, category),
//			title,
//			promo,
//			description,
//			date
//		);
//
//		em.persist(entity);
//		em.flush();
//
//		return entity;
//	}
//
//	@PUT
//	@Path("/i/{category}/entries/i/{id}")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public PublicationEntryEntity entriesUpdate(
//			@PathParam("id") long id,
//			@FormParam("category") long category,
//			@FormParam("title") String title,
//			@FormParam("promo") String promo,
//			@FormParam("description") String description,
//			@FormParam("date") LocalDate date
//	) {
//
//		PublicationEntryEntity entity = em.find(PublicationEntryEntity.class, id);
//
//		entity = new PublicationEntryEntity(
//			entity.getId(),
//			portal,
//			em.find(PublicationCategoryEntity.class, category),
//			title,
//			promo,
//			description,
//			date
//		);
//
//		em.merge(entity);
//
//		return entity;
//	}
//
//	@DELETE
//	@Path("/i/{category}/entries/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public void entriesDelete(
//			@PathParam("id") long id
//	) {
//
//		em.remove(em.find(PublicationEntryEntity.class, id));
//	}
//}
