package com.ntr1x.treasure.web.resources;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
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

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.p3.Publication;
import com.ntr1x.treasure.web.services.IPublicationService;
import com.ntr1x.treasure.web.services.IPublicationService.PublicationsResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Path("publications")
@Api("Publications")
@PermitAll
@Component
public class PublicationResource {
	
	@Inject
    private IPublicationService publications;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public PublicationsResponse list(
	        @QueryParam("page") @ApiParam(example = "0") int page,
			@QueryParam("size") @ApiParam(example = "10") int size
	) {
	    return publications.list(page, size);
	}
	
	@GET
	@Path("/query")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public PublicationsResponse query(
            @QueryParam("query") String query,
            @QueryParam("since") LocalDateTime since,
            @QueryParam("until") LocalDateTime until, 
            @QueryParam("category") List<String> categories,
            @QueryParam("page") @ApiParam(example = "0") int page,
            @QueryParam("size") @ApiParam(example = "10") int size
    ) {
	    
	    Long[][] array = null; {
	        
	        List<Long[]> list = new ArrayList<>();
	        for (String st : categories) {
	            
	            String[] parts = st.split("\\|");
	            if (parts.length > 0) {
	                Long[] item = new Long[parts.length];
	                for (int i = 0; i < parts.length; i++) {
	                    item[i] = Long.valueOf(parts[i]);
	                }
	                list.add(item);
	            }
	        }
	        
	        array = list.toArray(new Long[0][]);
	    }
	    
	    return publications.search(page, size, query, since, until, array);
    }
	
	@GET
	@Path("/i/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public PublicationsResponse.PublicationItem select(@PathParam("id") long id) {
	    
	    return publications.select(id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///publications:admin" })
	public Publication create(IPublicationService.PublicationCreate request) {

	    return publications.create(request);
	}
	
	@PUT
	@Path("/i/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///publications/i/{id}:admin" })
	public Publication update(@PathParam("id") long id, IPublicationService.PublicationUpdate request) {
	    
	    return publications.update(id, request);
	}
	
	@DELETE
	@Path("/i/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "res:///publications/{id}:admin" })
	public Publication remove(@PathParam("id") long id) {
		
		return publications.remove(id);
	}
}
