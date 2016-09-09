package com.ntr1x.treasure.web.resources;

import java.util.List;

import javax.annotation.security.PermitAll;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Depot;
import com.ntr1x.treasure.web.repository.DepotRepository;

import io.swagger.annotations.Api;

@Api("Depots")
@Component
@Path("depots")
@PermitAll
public class DepotResource {
    
    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private DepotRepository depots;
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public List<Depot> list() {
        return depots.findAll();
    }
}
