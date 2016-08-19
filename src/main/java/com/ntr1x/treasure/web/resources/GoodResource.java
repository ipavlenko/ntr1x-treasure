package com.ntr1x.treasure.web.resources;

import java.util.List;

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

import com.ntr1x.treasure.web.model.Good;
import com.ntr1x.treasure.web.model.Resource.CommentsView;
import com.ntr1x.treasure.web.model.Resource.TagsView;
import com.ntr1x.treasure.web.repository.GoodRepository;
import com.ntr1x.treasure.web.services.ResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Path("goods")
@Api("Goods")
@Component
public class GoodResource {
	
	@Inject
	private EntityManager em;
	
	@Inject
	private ResourceService service;
	
	@Inject
	private GoodRepository goods;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public List<Good> list(
    		@QueryParam("page") @ApiParam(example = "0") int page,
    		@QueryParam("size") @ApiParam(example = "10") int size
    ) {
		return goods.findAll(new PageRequest(page, size)).getContent();
    }
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Good select(@PathParam("id") long id) {
		return goods.findOne(id);
    }
	
	@GET
	@Path("/{id}/full")
	@CommentsView
	@TagsView
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Good selectFull(@PathParam("id") long id) {
		return goods.findOne(id);
    }
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Good create(Good good) {
	
		Good g = service.create(good, (r) -> String.format("/goods/%s", r.getId()), (r) -> r.getId() == null);
		em.clear();
		return goods.findOne(g.getId());
    }
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Good update(Good good) {
		
		Good g = service.update(good, (r) -> r.getId() == null);
		em.clear();
		return goods.findOne(g.getId());
    }
	
	@DELETE
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Good remove(@PathParam("id") long id) {
		
		Good g = goods.findOne(id);
		service.remove(g, (r) -> r.getId() != null);
		em.clear();
		return g;
    }
}
