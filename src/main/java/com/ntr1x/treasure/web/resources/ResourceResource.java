package com.ntr1x.treasure.web.resources;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.attributes.AttributeValue;
import com.ntr1x.treasure.web.model.purchase.ResourceType;
import com.ntr1x.treasure.web.model.security.SecurityResource;
import com.ntr1x.treasure.web.repository.ResourceRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Path("resources")
@Api("Resources")
@Component
public class ResourceResource {
	
	@Inject
	private ResourceRepository repository;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public List<SecurityResource> list(
    		@QueryParam("pattern") @ApiParam(example = "%") String pattern,
			@QueryParam("page") @ApiParam(example = "0") int page,
    		@QueryParam("size") @ApiParam(example = "10") int size
    ) {
		return (
			pattern == null
				? repository.findOrderByName(new PageRequest(page, size))
				: repository.findByNameLikeOrderByName(pattern, new PageRequest(page, size))
		).getContent();
    }
	
	@GET
	@Path("/i/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public SecurityResource select(@PathParam("id") long id) {
		return repository.findOne(id);
    }
	
	@GET
	@Path("/a/{alias}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public SecurityResource select(@PathParam("alias") String alias) {
		return repository.findByName(alias);
    }
	
	@GET
    @Path("/t/{type}/values")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
	public List<AttributeValue> selectValues(@PathParam("type") ResourceType type) {
	    
	    return repository.findByResType(type).getAttributes();
	}
}
