package com.ntr1x.treasure.web.resources;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Publication;
import com.ntr1x.treasure.web.model.Resource.AttachmentsView;
import com.ntr1x.treasure.web.model.Resource.CommentsView;
import com.ntr1x.treasure.web.model.Resource.TagsView;
import com.ntr1x.treasure.web.repository.PublicationRepository;
import com.ntr1x.treasure.web.services.ResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Path("publications")
@Api("Publications")
@PermitAll
@Component
public class PublicationResource {
	
	@Inject
	private EntityManager em;
	
	@Inject
	private ResourceService service;
	
	@Inject
	private PublicationRepository publications;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<Publication> list(
			@QueryParam("page") @ApiParam(example = "0") int page,
			@QueryParam("size") @ApiParam(example = "10") int size
	) {
		return publications.findAll(new PageRequest(0, size)).getContent();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Publication select(@PathParam("id") long id) {
		return publications.findOne(id);
	}
	
	@GET
	@Path("/{id}/full")
	@CommentsView
	@TagsView
	@AttachmentsView
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Publication selectFull(@PathParam("id") long id) {
		return publications.findOne(id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
//	@RolesAllowed({
//		"/companies:admin"
//	})
	public Publication create(Publication post) {

		Publication p = (Publication) service.create(null, post, "publications");
		em.clear();
		return publications.findOne(p.getId());
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
//	@RolesAllowed({
//		"/companies/{post.id}:admin"
//	})
	public Publication update(Publication post) {
		
		Publication p = (Publication) service.update(post);
		em.clear();
		return publications.findOne(p.getId());
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({
		"/companies/{post.id}:admin"
	})
	public Publication remove(@PathParam("id") long id) {
		
		Publication p = publications.findOne(id);
		service.remove(p);
		em.clear();
		return p;
	}
}
