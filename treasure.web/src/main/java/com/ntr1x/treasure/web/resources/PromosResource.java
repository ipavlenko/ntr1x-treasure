//package com.ntr1x.treasure.web.resources;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.OutputStream;
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
//import org.apache.commons.io.FileUtils;
//import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
//import org.glassfish.jersey.media.multipart.FormDataParam;
//
//import com.ntr1x.treasure.files.FileService;
//import com.ntr1x.treasure.images.ImageService;
//import com.ntr1x.treasure.model.content.BannerCategoryEntity;
//import com.ntr1x.treasure.model.content.BannerEntryEntity;
//import com.ntr1x.treasure.model.security.SecurityPortal;
//import com.ntr1x.treasure.web.providers.UserElementFilter.RequestAttribute;
//
//import io.swagger.annotations.Api;
//
//@Api("Promos")
//@RequestScoped
//@Path("/ws/content/promos")
//public class PromosResource {
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
//	@Inject @RequestAttribute
//	private SecurityPortal portal;
//
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public List<BannerCategoryEntity> bannersQuery() {
//
//		return em
//			.createNamedQuery("BannerCategoryEntity.accessible", BannerCategoryEntity.class)
//			.setParameter("portal", portal.getName())
//			.getResultList()
//			;
//	}
//
//	@GET
//	@Path("/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public BannerCategoryEntity categoriesReadById(
//			@PathParam("id") long id
//	) {
//
//		return em.find(BannerCategoryEntity.class, id);
//	}
//
//	@GET
//	@Path("/n/{name}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public BannerCategoryEntity categoriesReadByName(
//			@PathParam("name") String name
//	) {
//
//		return em
//				.createNamedQuery("BannerCategoryEntity.accessibleByName", BannerCategoryEntity.class)
//				.setParameter("portal", portal.getName())
//				.setParameter("name", name)
//				.getSingleResult()
//				;
//	}
//
//	@POST
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public BannerCategoryEntity categoriesCreate(
//			@FormParam("name") String name,
//			@FormParam("title") String title,
//			@FormParam("width") Integer width,
//			@FormParam("height") Integer height
//	) {
//
//		BannerCategoryEntity entity = new BannerCategoryEntity(
//				0L,
//				width,
//				height,
//				portal,
//				name,
//				title
//		);
//
//		em.persist(entity);
//		em.flush();
//		return entity;
//	}
//
//	@PUT
//	@Path("/i/{id}")
//	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public BannerCategoryEntity categoriesUpdate(
//			@PathParam("id") long id,
//			@FormParam("name") String name,
//			@FormParam("title") String title,
//			@FormParam("width") Integer width,
//			@FormParam("height") Integer height
//	) {
//
//		BannerCategoryEntity entity = em.find(BannerCategoryEntity.class, id);
//
//		entity = new BannerCategoryEntity(
//				entity.getId(),
//				width,
//				height,
//				portal,
//				name,
//				title
//		);
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
//		BannerCategoryEntity entity = em.find(BannerCategoryEntity.class, id);
//		em.remove(entity);
//	}
//
//	@GET
//	@Path("/i/{category}/entries")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public List<BannerEntryEntity> entriesQuery(
//			@PathParam("category") long category
//	) {
//
//		return em
//				.createNamedQuery("BannerEntryEntity.accessible", BannerEntryEntity.class)
//				.setParameter("portal", portal.getName())
//				.setParameter("category", category)
//				.getResultList()
//				;
//	}
//
//	@GET
//	@Path("/i/{category}/entries/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public BannerEntryEntity entriesRead(
//			@PathParam("id") long id
//	) {
//
//		return em.find(BannerEntryEntity.class, id);
//	}
//
//	@POST
//	@Path("/i/{category}/entries")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public BannerEntryEntity entriesCreate(
//			@PathParam("category") long category,
//			@FormDataParam("title") String title,
//			@FormDataParam("promo") String promo,
//			@FormDataParam("description") String description,
//			@FormDataParam("preview") InputStream previewInput,
//			@FormDataParam("preview") FormDataContentDisposition previewHeader
//	) {
//
//		BannerEntryEntity entity = new BannerEntryEntity(
//				0L,
//				portal,
//				em.find(BannerCategoryEntity.class, category),
//				title,
//				promo,
//				description
//		);
//
//		em.persist(entity);
//		em.flush();
//
//		File dir = files.resolve(String.format("%s/content/banners/%s", portal.getName(), entity.getId()));
//		dir.mkdirs();
//
//		String previewName = previewHeader.getFileName();
//		if (previewName != null && !previewName.isEmpty()) {
//
//			try (OutputStream previewOutput = new FileOutputStream(new File(dir, "preview.png"))) {
//
//				images.fitToWidth(previewInput, previewOutput, null, "png");
//
//			} catch (IOException e) {
//
//				throw new IllegalStateException(e);
//			}
//		}
//		return entity;
//	}
//
//	@PUT
//	@Path("/i/{category}/entries/i/{id}")
//	@Consumes(MediaType.MULTIPART_FORM_DATA)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public BannerEntryEntity entriesUpdate(
//			@PathParam("id") long id,
//			@FormDataParam("category") long category,
//			@FormDataParam("title") String title,
//			@FormDataParam("promo") String promo,
//			@FormDataParam("description") String description,
//			@FormDataParam("preview") InputStream previewInput,
//			@FormDataParam("preview") FormDataContentDisposition previewHeader
//	) {
//
//		BannerEntryEntity entity = em.find(BannerEntryEntity.class, id);
//
//		entity = new BannerEntryEntity(
//				entity.getId(),
//				portal,
//				em.find(BannerCategoryEntity.class, category),
//				title,
//				promo,
//				description
//		);
//
//		em.merge(entity);
//
//		File dir = files.resolve(String.format("%s/content/banners/%s", portal.getName(), entity.getId()));
//		dir.mkdirs();
//
//		String previewName = previewHeader.getFileName();
//		if (previewName != null && !previewName.isEmpty()) {
//
//			try (OutputStream previewOutput = new FileOutputStream(new File(dir, "preview.png"))) {
//
//				images.fitToWidth(previewInput, previewOutput, null, "png");
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
//	@Path("/i/{category}/entries/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public void entriesDelete(
//			@PathParam("id") long id
//	) {
//
//		BannerEntryEntity entity = em.find(BannerEntryEntity.class, id);
//		em.remove(entity);
//
//		FileUtils.deleteQuietly(files.resolve(String.format("%s/content/banners/%s", portal.getName(), entity.getId())));
//	}
//}
