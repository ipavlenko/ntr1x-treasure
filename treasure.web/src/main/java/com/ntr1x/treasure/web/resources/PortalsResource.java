//package com.ntr1x.treasure.web.resources;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
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
//import org.apache.commons.io.FileUtils;
//import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
//import org.glassfish.jersey.media.multipart.FormDataParam;
//
//import com.ntr1x.treasure.files.FileService;
//import com.ntr1x.treasure.images.ImageService;
//import com.ntr1x.treasure.model.catalog.PersonCategoryEntity;
//import com.ntr1x.treasure.model.catalog.PersonEntryEntity;
//import com.ntr1x.treasure.model.security.SecurityPortal;
//
//import io.swagger.annotations.Api;
//
//@Api("Portals")
//@Path("/ws/portals")
//public class PortalsResource {
//
//	@PersistenceContext(unitName="treasure.core")
//	private EntityManager em;
//
//	@Inject
//	private FileService files;
//
//	@Inject
//	private ImageService images;
//
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public List<SecurityPortal> portalsQuery() {
//
//		return em
//			.createNamedQuery("SecurityPortal.accessible", SecurityPortal.class)
//			.getResultList()
//		;
//	}
//
//	@GET
//	@Path("/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public SecurityPortal portalsRead(
//		@PathParam("id") long id
//	) {
//
//		return em.find(SecurityPortal.class, id);
//	}
//
//	@GET
//	@Path("/i/{id}/icon192x192")
//	@Transactional
//	public File portalsIcon192x192(
//			@PathParam("id") long id
//	) {
//
//		SecurityPortal entity = em.find(SecurityPortal.class, id);
//		return files.resolve(String.format("%s/icon192x192.png", entity.getId()));
//    };
//
//	@POST
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public SecurityPortal portalsCreate(
//			@FormDataParam("name") String name,
//			@FormDataParam("url") String url,
//			@FormDataParam("title") String title,
//			@FormDataParam("cookieDomain") String cookieDomain,
//			@FormDataParam("cookieName") String cookieName,
//			@FormDataParam("icon192x192") InputStream previewInput,
//			@FormDataParam("icon192x192") FormDataContentDisposition previewHeader
//	) {
//
//		SecurityPortal entity = new SecurityPortal(
//			0L,
//			name,
//			url,
//			title,
//			cookieDomain,
//			cookieName
//		);
//
//		em.persist(entity);
//		em.flush();
//
//		File dir = files.resolve(String.format("%s", entity.getId()));
//		dir.mkdirs();
//
//		String previewName = previewHeader.getFileName();
//		if (previewName != null && !previewName.isEmpty()) {
//
//			try (OutputStream previewOutput = new FileOutputStream(new File(dir, "icon192x192.png"))) {
//
//				images.fitToWidth(previewInput, previewOutput, 192, "png");
//
//			} catch (IOException e) {
//
//				throw new IllegalStateException(e);
//			}
//		}
//
//		return entity;
//	}
//
//	@PUT
//	@Path("/i/{id}")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public SecurityPortal portalsUpdate(
//			@PathParam("id") long id,
//			@FormDataParam("name") String name,
//			@FormDataParam("url") String url,
//			@FormDataParam("title") String title,
//			@FormDataParam("cookieDomain") String cookieDomain,
//			@FormDataParam("cookieName") String cookieName,
//			@FormDataParam("icon192x192") InputStream previewInput,
//			@FormDataParam("icon192x192") FormDataContentDisposition previewHeader
//	) {
//
//		SecurityPortal entity = em.find(SecurityPortal.class, id);
//
//		entity = new SecurityPortal(
//			entity.getId(),
//			name,
//			url,
//			title,
//			cookieDomain,
//			cookieName
//		);
//
//		em.merge(entity);
//
//		File dir = files.resolve(String.format("%s", entity.getId()));
//		dir.mkdirs();
//
//		String previewName = previewHeader.getFileName();
//		if (previewName != null && !previewName.isEmpty()) {
//
//			try (OutputStream previewOutput = new FileOutputStream(new File(dir, "icon192x192.png"))) {
//
//				images.fitToWidth(previewInput, previewOutput, 192, "png");
//
//			} catch (IOException e) {
//
//				throw new IllegalStateException(e);
//			}
//		}
//
//		return entity;
//	}
//
//	@DELETE
//	@Path("/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public void portalsDelete(
//		@PathParam("id") long id
//	) {
//
//		SecurityPortal entity = em.find(SecurityPortal.class, id);
//		em.remove(entity);
//
//		FileUtils.deleteQuietly(files.resolve(String.format("%s", entity.getId())));
//	}
//
//	@GET
//	@Path("/i/{portal}/users")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public List<PersonEntryEntity> usersQuery(
//			@PathParam("portal") long portal,
//			@PathParam("category") long category
//	) {
//
//		return em
//			.createNamedQuery("PersonEntryEntity.accessible", PersonEntryEntity.class)
//			.setParameter("portal", portal)
//			.setParameter("category", category)
//			.getResultList()
//		;
//	}
//
//	@GET
//	@Path("/i/{portal}/users/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public PersonEntryEntity usersRead(
//			@PathParam("portal") long portal,
//			@PathParam("id") long id
//	) {
//
//		return em.find(PersonEntryEntity.class, id);
//	}
//
//	@POST
//	@Path("/i/{portal}/users")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public PersonEntryEntity usersCreate(
//			@PathParam("portal") long portal,
//			@FormParam("category") long category,
//			@FormParam("gender") PersonEntryEntity.Gender gender,
//			@FormParam("name") String name,
//			@FormParam("promo") String promo,
//			@FormParam("description") String description
//	) {
//
//		PersonEntryEntity entity = new PersonEntryEntity(
//			0L,
//			em.find(SecurityPortal.class, portal),
//			em.find(PersonCategoryEntity.class, category),
//			gender,
//			name,
//			promo,
//			description
//		);
//
//		em.persist(entity);
//		em.flush();
//
//		return entity;
//	}
//
//	@PUT
//	@Path("/i/{portal}/users/i/{id}")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public PersonEntryEntity usersUpdate(
//			@PathParam("id") long id,
//			@PathParam("portal") long portal,
//			@FormParam("category") long category,
//			@FormParam("gender") PersonEntryEntity.Gender gender,
//			@FormParam("name") String name,
//			@FormParam("promo") String promo,
//			@FormParam("description") String description
//	) {
//
//		PersonEntryEntity entity = em.find(PersonEntryEntity.class, id);
//
//		entity = new PersonEntryEntity(
//			entity.getId(),
//			em.find(SecurityPortal.class, portal),
//			em.find(PersonCategoryEntity.class, category),
//			gender,
//			name,
//			promo,
//			description
//		);
//
//		em.merge(entity);
//
//		return entity;
//	}
//
//	@DELETE
//	@Path("/i/{portal}/users/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public void usersDelete(
//			@PathParam("portal") long portal,
//			@PathParam("id") long id
//	) {
//
//		em.remove(em.find(PersonEntryEntity.class, id));
//	}
//}