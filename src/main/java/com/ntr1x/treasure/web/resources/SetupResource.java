package com.ntr1x.treasure.web.resources;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.bootstrap.IBootstrap;
import com.ntr1x.treasure.web.bootstrap.IBootstrap.BootstrapResults;
import com.ntr1x.treasure.web.index.PublicationIndexRepository;
import com.ntr1x.treasure.web.repository.ResourceRepository;

import io.swagger.annotations.Api;

@Path("setup")
@Api("Setup")
@PermitAll
@Component
public class SetupResource {
    
    @Inject
    private ResourceRepository resources;
    
    @Inject
    private PublicationIndexRepository publications;
    
    @Inject
    private IBootstrap bootsrtap;
    
    @POST
    @Path("/bootstrap")
    public BootstrapResults bootstrap() {
        
        publications.deleteAll();
        resources.deleteAll();
        
        return bootsrtap.bootstrap();
    }
}
