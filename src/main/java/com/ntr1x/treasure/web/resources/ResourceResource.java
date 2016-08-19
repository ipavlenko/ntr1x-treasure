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

import com.ntr1x.treasure.web.model.Resource;
import com.ntr1x.treasure.web.model.Resource.AttachmentsView;
import com.ntr1x.treasure.web.model.Resource.CategoriesView;
import com.ntr1x.treasure.web.model.Resource.CommentsView;
import com.ntr1x.treasure.web.model.Resource.GoodsView;
import com.ntr1x.treasure.web.model.Resource.LikesView;
import com.ntr1x.treasure.web.model.Resource.SubcategoriesView;
import com.ntr1x.treasure.web.model.Resource.TagsView;
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
    public List<Resource> list(
    		@QueryParam("pattern") @ApiParam(example = "%") String pattern,
			@QueryParam("page") @ApiParam(example = "0") int page,
    		@QueryParam("size") @ApiParam(example = "10") int size
    ) {
		return (
			pattern == null
				? repository.findOrderByAlias(new PageRequest(page, size))
				: repository.findByAliasLikeOrderByAlias(pattern, new PageRequest(page, size))
		).getContent();
    }
	
	@GET
	@Path("/i/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Resource select(@PathParam("id") long id) {
		return repository.findOne(id);
    }
	
	@GET
	@Path("/a/{alias}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Resource select(@PathParam("alias") String alias) {
		return repository.findByAlias(alias);
    }
	
	@GET
	@Path("/i/{id}/full")
	@CommentsView
	@GoodsView
	@TagsView
	@AttachmentsView
	@CategoriesView
	@SubcategoriesView
	@LikesView
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Resource selectFull(@PathParam("id") long id) {
		return repository.findOne(id);
    }
	
	@GET
	@Path("/n/{id}/full")
	@CommentsView
	@GoodsView
	@TagsView
	@AttachmentsView
	@CategoriesView
	@SubcategoriesView
	@LikesView
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Resource selectFull(@PathParam("alias") String alias) {
		return repository.findByAlias(alias);
    }
}
