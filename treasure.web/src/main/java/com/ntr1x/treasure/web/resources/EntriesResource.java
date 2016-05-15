package com.ntr1x.treasure.web.resources;

import com.ntr1x.treasure.files.FileService;
import com.ntr1x.treasure.images.ImageService;
import com.ntr1x.treasure.model.catalog.Entry;
import com.ntr1x.treasure.model.catalog.EntryCategory;
import io.swagger.annotations.Api;
import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

@Api("Entries")
@RequestScoped
@Path("/ws/catalog/entries")
public class EntriesResource {

	@PersistenceContext(unitName="treasure.core")
	private EntityManager em;

	@Inject
	private FileService files;

	@Inject
	private ImageService images;

	@GET
    @Path("/categories")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<EntryCategory> categoriesQuery() {

		return em
			.createNamedQuery("EntryCategory.accessible", EntryCategory.class)
			.getResultList()
		;
	}

	@GET
	@Path("/categories/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public EntryCategory categoriesReadById(
			@PathParam("id") long id
	) {

		return em.find(EntryCategory.class, id);
	}

//	@GET
//	@Path("/n/{name}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public EntryCategory categoriesReadByName(
//			@PathParam("name") String name
//	) {
//
//		return em
//			.createNamedQuery("EntryCategory.accessibleByName", EntryCategory.class)
//			.setParameter("name", name)
//			.getSingleResult()
//		;
//	}

	@POST
	@Path("/categories")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public EntryCategory categoriesCreate(
            @FormParam("name") String name,
			@FormParam("title") String title,
			@FormParam("width") Integer width,
			@FormParam("height") Integer height
	) {

		EntryCategory entity = new EntryCategory(
			0L,
			width,
			height,
			name,
			title
		);

		em.persist(entity);
		em.flush();

		return entity;
	}

	@PUT
	@Path("/categories/{id}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public EntryCategory categoriesUpdate(
			@PathParam("id") long id,
			@FormParam("name") String name,
			@FormParam("title") String title,
			@FormParam("width") Integer width,
			@FormParam("height") Integer height
	) {

		EntryCategory entity = em.find(EntryCategory.class, id);

		entity = new EntryCategory(
			entity.getId(),
			width,
			height,
			name,
			title
		);

		em.merge(entity);

		return entity;
	}

	@DELETE
	@Path("/categories/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public void categoriesDelete(
			@PathParam("id") long id
	) {

		EntryCategory entity = em.find(EntryCategory.class, id);
		em.remove(entity);
	}

	@GET
	@Path("/items")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<Entry> itemsQuery(
			@QueryParam("category") long category
	) {

		List<Entry> entries = em
				.createNamedQuery("Entry.accessible", Entry.class)
				.setParameter("category", category)
				.getResultList();

		return entries;
	}

	@GET
	@Path("/items/{id}")
	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
	public Entry itemsRead(
			@PathParam("id") long id
	) {

		return em.find(Entry.class, id);
	}

	@GET
	@Path("/items/{id}/preview")
//	@Transactional
	@Produces("stream/png")
	public File itemsPreview(
			@PathParam("id") long id
	) {

		return files.resolve(String.format("catalog/entries/%s/preview.png", id));
    };

	@POST
	@Path("/items")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Entry itemsCreate(
			@FormDataParam("category") long category,
            @FormDataParam("price") double price,
			@FormDataParam("title") String title,
			@FormDataParam("promo") String promo,
			@FormDataParam("description") String description,
			@FormDataParam("preview") InputStream previewInput,
			@FormDataParam("preview") FormDataContentDisposition previewHeader
	) {

		Entry entity = new Entry(
			0L,
			em.find(EntryCategory.class, category),
            price,
			title,
			promo,
			description
            //, null
		);

		em.persist(entity);
		em.flush();

		File dir = files.resolve(String.format("catalog/entries/%s", entity.getId()));
		dir.mkdirs();

		if (previewHeader != null && previewHeader.getFileName() != null && !previewHeader.getFileName().isEmpty()) {

			try (OutputStream previewOutput = new FileOutputStream(new File(dir, "preview.png"))) {

				images.fitToWidth(previewInput, previewOutput, null, "png");

			} catch (IOException e) {

				throw new IllegalStateException(e);
			}
		}

		return entity;
	}

	@PUT
	@Path("/items/{id}")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Entry itemsUpdate(
			@PathParam("id") long id,
			@FormDataParam("category") long category,
            @FormDataParam("price") double price,
            @FormDataParam("title") String title,
			@FormDataParam("promo") String promo,
			@FormDataParam("description") String description,
			@FormDataParam("preview") InputStream previewInput,
			@FormDataParam("preview") FormDataContentDisposition previewHeader
	) {

		Entry entity = em.find(Entry.class, id);

		entity = new Entry(
			entity.getId(),
			em.find(EntryCategory.class, category),
            price,
            title,
			promo,
			description
//            , entity.getOffers()
		);

		em.merge(entity);

		File dir = files.resolve(String.format("catalog/entries/%s", entity.getId()));
		dir.mkdirs();

		if (previewHeader != null && previewHeader.getFileName() != null && !previewHeader.getFileName().isEmpty()) {

			try (OutputStream previewOutput = new FileOutputStream(new File(dir, "preview.png"))) {

				images.fitToWidth(previewInput, previewOutput, null, "png");

			} catch (IOException e) {

				throw new IllegalStateException(e);
			}
		}

		return entity;
	}

	@DELETE
	@Path("/items/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public void itemsDelete(
			@PathParam("id") long id
	) {

		Entry entity = em.find(Entry.class, id);
		em.remove(entity);

		FileUtils.deleteQuietly(files.resolve(String.format("catalog/entries/%s", entity.getId())));
	}
}
