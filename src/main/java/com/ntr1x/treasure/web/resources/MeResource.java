package com.ntr1x.treasure.web.resources;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Cart;
import com.ntr1x.treasure.web.model.Session;

import io.swagger.annotations.Api;

@Api("Me")
@Component
@Path("me")
@PermitAll
public class MeResource {

    @Inject
    private Session session;
    
    @GET
    @Path("/cart")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public Cart select() {
        return session.getUser().getCart();
    }
}
