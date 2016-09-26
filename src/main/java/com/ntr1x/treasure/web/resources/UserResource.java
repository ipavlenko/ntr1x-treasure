package com.ntr1x.treasure.web.resources;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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

import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.services.IUserService;
import com.ntr1x.treasure.web.services.IUserService.CreateUser;
import com.ntr1x.treasure.web.services.IUserService.UpdateUser;
import com.ntr1x.treasure.web.services.IUserService.UsersResponse;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;


@Api("Users")
@Component
@Path("/users")
@PermitAll
public class UserResource {
	
    @PersistenceContext
    private EntityManager em;

    @Inject
    private IUserService users;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///users:admin" })
    public UsersResponse list(
        @QueryParam("page") @ApiParam(example = "0") int page,
        @QueryParam("size") @ApiParam(example = "10") int size
    ) {
        return users.list(page, size);
    }

    @GET
    @Path("/i/{id}/lock")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///users/i/{id}:admin" })
    public User lock(@PathParam("id") long id) {

        User user = em.find(User.class, id);
        user.setLocked(true);
        
        em.persist(user);
        em.flush();

        return user;
    }

    @GET
    @Path("/i/{id}/unlock")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed( { "res:///users/i/{id}:admin" })
    public User unlock(@PathParam("id") long id) {

        User user = em.find(User.class, id);
        user.setLocked(false);
        
        em.persist(user);
        em.flush();

        return user;
    }

    @GET
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///users/i/{id}:admin" })
    public User select(@PathParam("id") long id) {
        
        return users.select(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public User create(CreateUser user) {
        
        return users.create(user);
	}

	@PUT
	@Path("/i/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public User update(@PathParam("id") long id, UpdateUser user) {
	    
	    return users.update(id, user);
	}
	
	@DELETE
    @Path("/i/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    @RolesAllowed({ "res:///users/i/{id}:admin" })
    public User remove(@PathParam("id") long id) {
        
	    return users.remove(id);
    }
}
