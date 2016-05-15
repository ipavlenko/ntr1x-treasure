package com.ntr1x.treasure.web.resources;

import com.ntr1x.treasure.files.FileService;
import com.ntr1x.treasure.images.ImageService;
import com.ntr1x.treasure.model.catalog.Offer;
import com.ntr1x.treasure.model.catalog.OfferCategory;
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

@Api("Offers")
@RequestScoped
@Path("/ws/catalog/offers")
public class OffersResource {

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
    public List<OfferCategory> categoriesQuery() {

        return em
                .createNamedQuery("OfferCategory.list", OfferCategory.class)
                .getResultList()
                ;
    }

    @GET
    @Path("/categories/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public OfferCategory categoriesReadById(
            @PathParam("id") long id
    ) {

        return em.find(OfferCategory.class, id);
    }

    @POST
    @Path("/categories")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public OfferCategory categoriesCreate(
            @FormParam("name") String name,
            @FormParam("title") String title,
            @FormParam("width") Integer width,
            @FormParam("height") Integer height
    ) {

        OfferCategory entity = new OfferCategory(
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
    public OfferCategory categoriesUpdate(
            @PathParam("id") long id,
            @FormParam("name") String name,
            @FormParam("title") String title,
            @FormParam("width") Integer width,
            @FormParam("height") Integer height
    ) {

        OfferCategory entity = em.find(OfferCategory.class, id);

        entity = new OfferCategory(
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

        em.remove(em.find(OfferCategory.class, id));
    }

    @GET
    @Path("/items")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Offer> itemsQuery(
            @QueryParam("category") long category
    ) {

        return category == 0 ?
                em
                    .createNamedQuery("Offer.list", Offer.class)
                    .getResultList()
                :
                em
                    .createNamedQuery("Offer.accessibleOfCategory", Offer.class)
                    .setParameter("category", category)
                    .getResultList()
                ;
    }

    @GET
    @Path("/items/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Offer itemsRead(
            @PathParam("id") long id
    ) {

        return em.find(Offer.class, id);
    }

    @GET
    @Path("/items/{id}/preview")
    @Transactional
    @Produces("stream/png")
    public File itemsPreview(
            @PathParam("id") long id
    ) {

        return files.resolve(String.format("catalog/offers/%s/preview.png", id));
    };

    @POST
    @Path("/items")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Offer itemsCreate(
            @FormDataParam("category") long category,
            @FormDataParam("name") String name,
            @FormDataParam("description") String description,
            @FormDataParam("preview") InputStream previewInput,
            @FormDataParam("preview") FormDataContentDisposition previewHeader
    ) {

        Offer entity = new Offer(
                0L,
                em.find(OfferCategory.class, category),
                name,
                description
        );

        em.persist(entity);
        em.flush();

        File dir = files.resolve(String.format("catalog/offers/%s", entity.getId()));
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
    public Offer itemsUpdate(
            @PathParam("id") long id,
            @FormDataParam("category") long category,
            @FormDataParam("name") String name,
            @FormDataParam("description") String description,
            @FormDataParam("preview") InputStream previewInput,
            @FormDataParam("preview") FormDataContentDisposition previewHeader
    ) {

        Offer entity = em.find(Offer.class, id);

        entity = new Offer(
                entity.getId(),
                em.find(OfferCategory.class, category),
                name,
                description
        );

        em.merge(entity);

        File dir = files.resolve(String.format("catalog/offers/%s", entity.getId()));
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

        FileUtils.deleteQuietly(files.resolve(String.format("catalog/offers/%s", entity.getId())));
    }
}
