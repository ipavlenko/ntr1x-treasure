package com.ntr1x.treasure.web.resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import javax.annotation.security.RolesAllowed;
import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.converter.ImageSettingsProvider.ImageSettings;
import com.ntr1x.treasure.web.model.Image;
import com.ntr1x.treasure.web.model.Session;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.UploadRepository;
import com.ntr1x.treasure.web.services.IFileService;
import com.ntr1x.treasure.web.services.IImageService;
import com.ntr1x.treasure.web.services.ISecurityService;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Api("Images")
@Component
@Path("/images")
public class ImageResource {

    @Inject
    private IFileService files;

    @Inject
    private IImageService images;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private Session session;
    
    @Inject
    private UploadRepository uploads;
    
    @PersistenceContext
    private EntityManager em;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({ "auth" })
    @Transactional
    public Image upload(
			@FormDataParam("file") InputStream stream,
			@FormDataParam("file") FormDataContentDisposition header,
			@FormDataParam("settings") ImageSettings settings
    ) {
        
        Image image = new Image(); {
            image.setUuid(UUID.randomUUID());
            image.setOriginal(header == null ? null : header.getFileName());
        };
        
        em.persist(image);
        em.flush();
        
        security.register(image, ResourceUtils.alias(null, "images/i", image));
        security.grant(session.getUser(), image.getAlias(), "admin");
        
        try {
            
            File dir = files.resolve(String.format("%s/%s", image.getDir(), image.getUuid())); {
                dir.mkdirs();
            }
        
            File source = Files.createTempFile("upload", ".tmp").toFile(); {
                FileUtils.copyInputStreamToFile(stream, source);
            }
            
            for (ImageSettings.Item item : settings.items) {
                
                File target = new File(dir, String.format("%s.%s", item.name, item.format));
                
                ImageIO.write(
                    images.scale(
                        ImageIO.read(source),
                        item.type,
                        item.width,
                        item.height
                    ),
                    "png",
                    target
                );
            }
            
        } catch (IOException e) {
            
            throw new WebApplicationException(Response.Status.INTERNAL_SERVER_ERROR);
        }

        return image;
    }
    
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public UploadsResponse list(
	    @QueryParam("aspect") String aspect,
		@QueryParam("page") int page,
		@QueryParam("size") int size
	) {
	    
	    Page<Image> p = uploads.findByAspect(aspect, new PageRequest(page, size));
	    
		return new UploadsResponse(
		    p.getTotalElements(),
		    page,
		    size,
		    p.getContent()
		);
	}
	
	@GET
	@Path("/i/{id}/{name}.{format}")
    @Transactional
    public Response selectImage(
        @PathParam("id") long id,
        @PathParam("name") String name,
        @PathParam("format") String format
    ) {
	    
	    Image upload = em.find(Image.class, id);
	    
        File file = files.resolve(String.format("%s/%s/%s.%s", upload.getDir(), upload.getUuid(), name, format));
        
        return Response
            .ok(file)
            .header("Content-Type", String.format("image/%s", format))
            .build()
        ;
    }
	
	@GET
    @Path("/u/{uuid}/{name}.{format}")
    @Transactional
    public Response selectImage(
        @PathParam("uuid") UUID uuid,
        @PathParam("name") String name,
        @PathParam("name") String format
    ) {
        
        Image upload = uploads.findByUuid(uuid);
        
        File file = files.resolve(String.format("%s/%s/%s.%s", upload.getDir(), upload.getUuid(), name, format));
        
        return Response
            .ok(file)
            .header("Content-Type", String.format("image/%s", format))
            .build()
        ;
    }
	
	@GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Image select(
        @PathParam("id") long id
    ) {
        Image upload = em.find(Image.class, id);
        return upload;
    }
	
	@GET
    @Path("/u/{uuid}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Image select(
        @PathParam("uuid") UUID uuid
    ) {
	    Image upload = uploads.findByUuid(uuid);
        return upload;
    }

	@XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UploadsResponse {
	    
        public long count;
        public int page;
        public int size;
        public List<Image> uploads;
    }
}
