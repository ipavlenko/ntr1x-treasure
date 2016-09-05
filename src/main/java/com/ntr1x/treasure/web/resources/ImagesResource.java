package com.ntr1x.treasure.web.resources;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.CommonConfig;
import com.ntr1x.treasure.web.converter.ImageSettingsProvider;
import com.ntr1x.treasure.web.model.assets.Upload;
import com.ntr1x.treasure.web.services.FileService;
import com.ntr1x.treasure.web.services.ImageService;
import com.ntr1x.treasure.web.utils.ConversionUtils;
import com.ntr1x.treasure.web.utils.UuidHelper;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Api("Images")
@Component
@Path("/ws/files/images")
public class ImagesResource {

    @Inject
    private FileService files;

    @Inject
    private ImageService images;

    @Context
    private HttpHeaders header;
    @Context
    private HttpServletResponse response;

    @PersistenceContext
    private EntityManager em;

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Uploads {
		private long count;
		private int offset;
		private int limit;
		private List<Upload> uploads;
	}

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
	@Path("/upload/square")
    @Transactional
    public Upload uploadImageSquare(
			@FormDataParam("size") Integer side,
			@FormDataParam("preview") InputStream inputStream,
			@FormDataParam("preview") FormDataContentDisposition inputHeader,
			@FormDataParam("type") Upload.Type type,
			@FormDataParam("settings") String settings	// TODO Add provider for ImageSettingsProvider.ImageRectSettings
    ) throws IllegalStateException {

        if (side == null || side <= 0 || side > 1000) {
			side = CommonConfig.IMG_SQARE_DEFAULT_SIZE;
        }

		ImageSettingsProvider.ImageSquareSettings result = ImageSettingsProvider.convertToSquareSettings(settings);

        String base62UUID = ConversionUtils.BASE62.encode(
                UuidHelper.getBytesFromUUID(UUID.randomUUID())
        );

        Upload upload = new Upload(
                0L,
                base62UUID,
                inputHeader != null ? inputHeader.getFileName() : "",
				type != null ? type : Upload.Type.IMAGE										//TODO брать из параметра settings (сделать этот параметр не обязательным, по умолчанию IMAGE)
        );

        em.persist(upload);
        em.flush();

        File dir = files.resolve("images/original");
        dir.mkdirs();

		File previewsDir = null;
		if (result != null && result.items != null && !result.items.isEmpty() ) {
			previewsDir = files.resolve(String.format("images/preview/%s", upload.getUuid()));
			previewsDir.mkdirs();
		}

        if (inputHeader != null && inputHeader.getFileName() != null && !inputHeader.getFileName().isEmpty()) {

			BufferedImage src = null;

			try (OutputStream previewOutput = new FileOutputStream(new File(dir, upload.getUuid() + ".png"))) {

				src = ImageIO.read(inputStream);

                images.fitSquare(
						images.getImageInputStream(src),
						previewOutput,
						side,
						"png"
				);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }


			if (result != null && result.items != null) {

				for (ImageSettingsProvider.ImageSquareSettingsItem item : result.items) {
					try (OutputStream previewOutput = new FileOutputStream(new File(previewsDir, item.getName() + ".png"))) {
						images.fitSquare(
								images.getImageInputStream(src),
								previewOutput,
								item.getSide(),
								"png"
						);
					} catch (IOException e) {
						throw new IllegalStateException(e);
					}
				}
			}

        }

        return upload;
    }

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/upload/width")
	@Transactional
	public Upload uploadImageToWidth(
			@FormDataParam("width") Integer width,
			@FormDataParam("preview") InputStream inputStream,
			@FormDataParam("preview") FormDataContentDisposition inputHeader,
			@FormDataParam("type") Upload.Type type,
			@FormDataParam("settings") String settings	// TODO Add provider for ImageSettingsProvider.ImageWidthSettings
	) throws IllegalStateException {

		if (width == null || width <= 0 || width > 1000) {
			width = CommonConfig.IMG_RECT_DEFAULT_WIDTH;
		}

		ImageSettingsProvider.ImageWidthSettings result = ImageSettingsProvider.convertToWidthSettings(settings);

		String base62UUID = ConversionUtils.BASE62.encode(
				UuidHelper.getBytesFromUUID(UUID.randomUUID())
		);

		Upload upload = new Upload(
				0L,
				base62UUID,
				inputHeader != null ? inputHeader.getFileName() : "",
				type != null ? type : Upload.Type.IMAGE										//TODO брать из параметра settings (сделать этот параметр не обязательным, по умолчанию IMAGE)
		);

		em.persist(upload);
		em.flush();

		File dir = files.resolve("images/original");
		dir.mkdirs();
		File previewsDir = null;
		if (result != null && result.items != null && !result.items.isEmpty() ) {
			previewsDir = files.resolve(String.format("images/preview/%s", upload.getUuid()));
			previewsDir.mkdirs();
		}

		if (inputHeader != null && inputHeader.getFileName() != null && !inputHeader.getFileName().isEmpty()) {

			BufferedImage src = null;

			try (OutputStream previewOutput = new FileOutputStream(new File(dir, upload.getUuid() + ".png"))) {
				src = ImageIO.read(inputStream);
				images.fitToWidth(
						images.getImageInputStream(src),
						previewOutput,
						width,
						"png"
				);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}

			if (result != null && result.items != null) {

				for (ImageSettingsProvider.ImageWidthSettingsItem item : result.items) {
					try (OutputStream previewOutput = new FileOutputStream(new File(previewsDir, item.getName() + ".png"))) {
						images.fitToWidth(
								images.getImageInputStream(src),
								previewOutput,
								item.getWidth(),
								"png"
						);
					} catch (IOException e) {
						throw new IllegalStateException(e);
					}
				}
			}

		}

		return upload;
	}

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/upload/rect")
	@Transactional
	public Upload uploadImageRect(
			@FormDataParam("width") Integer width,
			@FormDataParam("height") Integer height,
			@FormDataParam("preview") InputStream inputStream,
			@FormDataParam("preview") FormDataContentDisposition previewHeader,
			@FormDataParam("type") Upload.Type type,
			@FormDataParam("settings") String settings	// TODO Add provider for ImageSettingsProvider.ImageRectSettings
	) throws IllegalStateException {

		if (width == null || width <= 0 || width > 1000) {
			width = CommonConfig.IMG_RECT_DEFAULT_WIDTH;
		}
		if (height == null || height <= 0 || height > 1000) {
			height = CommonConfig.IMG_RECT_DEFAULT_HEIGHT;
		}

		ImageSettingsProvider.ImageRectSettings result = ImageSettingsProvider.convertToRectSettings(settings);

		String base62UUID = ConversionUtils.BASE62.encode(
				UuidHelper.getBytesFromUUID(UUID.randomUUID())
		);

		Upload upload = new Upload(
				0L,
				base62UUID,
				previewHeader != null ? previewHeader.getFileName() : "",
				type != null ? type : Upload.Type.IMAGE											//TODO брать из параметра settings (сделать этот параметр не обязательным, по умолчанию IMAGE)
		);

		em.persist(upload);
		em.flush();

		File dir = files.resolve("images/original");
		dir.mkdirs();
		File previewsDir = null;
		if (result != null && result.items != null && !result.items.isEmpty() ) {
			previewsDir = files.resolve(String.format("images/preview/%s", upload.getUuid()));
			previewsDir.mkdirs();
		}

		if (previewHeader != null && previewHeader.getFileName() != null && !previewHeader.getFileName().isEmpty()) {

			BufferedImage src = null;

			try (OutputStream previewOutput = new FileOutputStream(new File(dir, upload.getUuid() + ".png"))) {
				src = ImageIO.read(inputStream);
				images.fit(
						images.getImageInputStream(src),
						previewOutput,
						width,
						height,
						"png"
				);
			} catch (IOException e) {
				throw new IllegalStateException(e);
			}

			if (result != null && result.items != null) {

				for (ImageSettingsProvider.ImageRectSettingsItem item : result.items) {
					try (OutputStream previewOutput = new FileOutputStream(new File(previewsDir, item.getName() + ".png"))) {
						images.fit(
								images.getImageInputStream(src),
								previewOutput,
								item.getWidth(),
								item.getHeight(),
								"png"
						);
					} catch (IOException e) {
						throw new IllegalStateException(e);
					}
				}
			}

		}

		return upload;
	}

    @GET
    @Path("/items/{UUID}")
    @Transactional
    @Produces("image/png")
    public Response getImage(
			@PathParam("UUID") String uuid
	) throws IOException {

        File img = files.resolve("images/original/" + uuid + ".png");
        BufferedImage image = ImageIO.read(img);

        if (image != null) {

            response.setContentType("images/png");
            response.setHeader("Content-Type", uuid);
            response.setHeader("Content-Disposition", "inline; filename=\"" + uuid + "\"");

            OutputStream out = response.getOutputStream();
            ImageIO.write(image, "png", out);
            out.close();

            return Response.ok().build();
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/items/{UUID}/{name}")
    @Transactional
	@Produces("image/png")
    public Response getImagePreview(
			@PathParam("UUID") String uuid,
            @PathParam("name") String name
    ) throws IOException {

		if (files.resolve(String.format("images/preview/%s/", uuid)).exists()) {

			File img = files.resolve(String.format("images/preview/%s/%s.png", uuid, name));
			BufferedImage image = ImageIO.read(img);

			if (image != null) {

				response.setContentType("images/png");
				response.setHeader("Content-Type", name);
				response.setHeader("Content-Disposition", "inline; filename=\"" + name + "\"");

				OutputStream out = response.getOutputStream();
				ImageIO.write(image, "png", out);
				out.close();

				return Response.ok().build();
			}
		}
		return Response.noContent().build();
    }

	@GET
	@Path("/items")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional()
//	@RolesAllowed("admin")
	public Response items(
			@QueryParam("limit") int limit,
			@QueryParam("offset") int offset
	){
		CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

		CriteriaQuery criteriaQuery = criteriaBuilder.createQuery();
		Root upload = criteriaQuery.from(Upload.class);

		Query query = em.createQuery(criteriaQuery);
		query.setMaxResults(limit);
		query.setFirstResult(offset);

		List<Upload> result = query.getResultList();

		criteriaQuery.select(criteriaBuilder.count(upload));
		query = em.createQuery(criteriaQuery);
		long count = (Long)query.getSingleResult();

		return Response.ok(
				new Uploads(
						count,
						offset,
						limit,
						result
				)
		).build();
	}


}
