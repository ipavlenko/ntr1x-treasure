package com.ntr1x.treasure.web.resources;

import com.ntr1x.treasure.files.FileService;
import com.ntr1x.treasure.images.ImageService;
import com.ntr1x.treasure.model.catalog.Offer;
import com.ntr1x.treasure.model.catalog.Person;
import com.ntr1x.treasure.model.catalog.PersonCategory;
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

@Api("Persons")
@RequestScoped
@Path("/ws/catalog/persons")
public class PersonsResource {

    @Inject
    private FileService files;

    @Inject
    private ImageService images;

	@PersistenceContext(unitName="treasure.core")
	private EntityManager em;

	@GET
    @Path("/categories")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<PersonCategory> categoriesQuery() {
		
		return em
			.createNamedQuery("PersonCategory.accessible", PersonCategory.class)
			.getResultList()
		;
	}
	
	@GET
	@Path("/categories/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public PersonCategory categoriesReadById(
			@PathParam("id") long id
	) {

		return em.find(PersonCategory.class, id);
	}

	@POST
    @Path("/categories")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public PersonCategory categoriesCreate(
			@FormParam("name") String name,
			@FormParam("title") String title,
			@FormParam("width") Integer width,
			@FormParam("height") Integer height
	) {
		
		PersonCategory entity = new PersonCategory(
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
	public PersonCategory categoriesUpdate(
			@PathParam("id") long id,
			@FormParam("name") String name,
			@FormParam("title") String title,
			@FormParam("width") Integer width,
			@FormParam("height") Integer height
	) {
		
		PersonCategory entity = em.find(PersonCategory.class, id);
		
		entity = new PersonCategory(
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
		
		em.remove(em.find(PersonCategory.class, id));
	}
	
	@GET
	@Path("/items")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<Person> itemsQuery(
			@QueryParam("category") long category
	) {
		
		return category == 0 ?
                em
			    .createNamedQuery("Person.accessible", Person.class)
			    .getResultList()
                :
                em
                .createNamedQuery("Person.accessibleOfCategory", Person.class)
                .setParameter("category", category)
                .getResultList()
		;
	}
	
	@GET
	@Path("/items/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Person itemsRead(
			@PathParam("id") long id
	) {
		
		return em.find(Person.class, id);
	}

    @GET
    @Path("/items/{id}/preview")
    @Transactional
    @Produces("stream/png")
    public File itemsPreview(
            @PathParam("id") long id
    ) {

        return files.resolve(String.format("catalog/persons/%s/preview.png", id));
    };
	
	@POST
	@Path("/items")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Person itemsCreate(
			@FormDataParam("category") long category,
			@FormDataParam("gender") Person.Gender gender,
			@FormDataParam("name") String name,
			@FormDataParam("description") String description,
            @FormDataParam("preview") InputStream previewInput,
            @FormDataParam("preview") FormDataContentDisposition previewHeader
	) {
		
		Person entity = new Person(
			0L,
			em.find(PersonCategory.class, category),
			gender,
			name,
			description
		);
		
		em.persist(entity);
		em.flush();

        File dir = files.resolve(String.format("catalog/persons/%s", entity.getId()));
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
	public Person itemsUpdate(
			@PathParam("id") long id,
			@FormDataParam("category") long category,
			@FormDataParam("gender") Person.Gender gender,
			@FormDataParam("name") String name,
			@FormDataParam("description") String description,
            @FormDataParam("preview") InputStream previewInput,
            @FormDataParam("preview") FormDataContentDisposition previewHeader
	) {
		
		Person entity = em.find(Person.class, id);
		
		entity = new Person(
			entity.getId(),
			em.find(PersonCategory.class, category),
			gender,
			name,
			description
		);
		
		em.merge(entity);

        File dir = files.resolve(String.format("catalog/persons/%s", entity.getId()));
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

        Offer entity = em.find(Offer.class, id);
        em.remove(entity);

        FileUtils.deleteQuietly(files.resolve(String.format("catalog/persons/%s", entity.getId())));
	}
}
