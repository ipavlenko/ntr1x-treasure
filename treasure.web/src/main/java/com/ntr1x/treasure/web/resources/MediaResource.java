package com.ntr1x.treasure.web.resources;

import com.ntr1x.treasure.files.FileService;
import com.ntr1x.treasure.images.ImageService;
import com.ntr1x.treasure.model.catalog.Media;
import com.ntr1x.treasure.model.catalog.MediaCategory;
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
import java.time.LocalDateTime;
import java.util.List;

@Api("Media")
@RequestScoped
@Path("/ws/catalog/media")
public class MediaResource {

    @Inject
    private FileService files;

    @Inject
    private ImageService images;

    @PersistenceContext(unitName="treasure.core")
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/categories")
    public List<MediaCategory> categoriesList() {
        return em
                .createNamedQuery("MediaCategory.list", MediaCategory.class)
                .getResultList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/categories")
    @Transactional
    public MediaCategory categoryCreate(
            @FormParam("name") String name,
            @FormParam("title") String title,
            @FormParam("height") Integer height,
            @FormParam("width") Integer width
    ) {

        MediaCategory entity = new MediaCategory(
                0L,
                name,
                title,
                height,
                width
        );

        em.persist(entity);
        em.flush();

        return entity;
    }

    @GET
    @Path("/categories/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public MediaCategory categoriesReadById(
            @PathParam("id") long category
    ) {
        return em.find(MediaCategory.class, category);
    }

    @PUT
    @Path("/categories/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public MediaCategory categoriesUpdate(
            @PathParam("id") long id,
            @FormParam("name") String name,
            @FormParam("title") String title,
            @FormParam("width") Integer width,
            @FormParam("height") Integer height
    ) {

        MediaCategory entity = em.find(MediaCategory.class, id);

        entity = new MediaCategory(
                entity.getId(),
                name,
                title,
                width,
                height
        );

        em.merge(entity);

        return entity;
    }

    @DELETE
    @Path("/categories/{id}")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Transactional
    public void categoriesDelete(
            @PathParam("id") long id
    ) {
        MediaCategory entity = em.find(MediaCategory.class, id);
        em.remove(entity);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @Path("/items")
    public List<Media> itemsList(
            @QueryParam("category") long category
    ) {
        try {
            List<Media> list = (category == 0
                    ? em
                    .createNamedQuery("Media.accessible")
                    .getResultList()
                    : em
                    .createNamedQuery("Media.mediaOfCategory")
                    .setParameter("category", category)
                    .getResultList()
            );

            return list;
        } catch(Exception e) {
            return null;
        }
    }

    @GET
    @Path("/items/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Media itemsReadById(
            @PathParam("id") Long media
    ) {
        return em.find(Media.class, media);
    }

    @GET
    @Path("/items/{id}/preview")
    @Transactional
    @Produces("stream/png")
    public File itemsPreview(
            @PathParam("id") long id
    ) {

        return files.resolve(String.format("catalog/media/%s/preview.png", id));
    };

    @POST
    @Path("/items")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Media itemsCreate(
            @FormDataParam("category") long category,
            @FormDataParam("title") String title,
            @FormDataParam("description") String description,
            @FormDataParam("preview") InputStream previewInput,
            @FormDataParam("preview") FormDataContentDisposition previewHeader
    ) {

        Media entity = new Media(
                0L,
                em.find(MediaCategory.class, category),
                title,
                description,
                LocalDateTime.now()
        );

        em.persist(entity);
        em.flush();

        File dir = files.resolve(String.format("catalog/media/%s", entity.getId()));
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
    @Transactional
    public void itemsDelete(@PathParam("id") long id) {
        Media entity = em.find(Media.class, id);
        em.remove(entity);

        FileUtils.deleteQuietly(files.resolve(String.format("catalog/media/%s", entity.getId())));
    }

    @PUT
    @Path("/items/{id}")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Media itemsUpdate(
        @PathParam("id") long id,
        @FormDataParam("category") long category,
        @FormDataParam("title") String title,
        @FormDataParam("description") String description,
        @FormDataParam("preview") InputStream previewInput,
        @FormDataParam("preview") FormDataContentDisposition previewHeader
    ) {
        Media entity = em.find(Media.class, id);

        entity = new Media(
                entity.getId(),
                em.find(MediaCategory.class, category),
                title,
                description,
                entity.getPublished()
        );

        em.merge(entity);

        File dir = files.resolve(String.format("catalog/media/%s", entity.getId()));
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

}
