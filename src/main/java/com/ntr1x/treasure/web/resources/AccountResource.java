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

import com.ntr1x.treasure.web.model.Account;
import com.ntr1x.treasure.web.model.Resource.AttachmentsView;
import com.ntr1x.treasure.web.model.Resource.CommentsView;
import com.ntr1x.treasure.web.model.Resource.TagsView;
import com.ntr1x.treasure.web.repository.AccountRepository;
import com.ntr1x.treasure.web.services.ISecurityService;
import com.ntr1x.treasure.web.services.ResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Path("accounts")
@Api("Accounts")
@PermitAll
@Component
public class AccountResource {
	
	@Inject
	private EntityManager em;
	
	@Inject
	private ResourceService service;
	
	@Inject
	private AccountRepository accounts;
	
	@Inject
	private ISecurityService security;
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({
	    "res:///accounts:admin"
	})
	@Transactional
    public List<Account> list(
    		@QueryParam("page") @ApiParam(example = "0") int page,
    		@QueryParam("size") @ApiParam(example = "10") int size
    ) {
		return accounts.findAll(new PageRequest(0, size)).getContent();
    }
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({
        "res:///accounts:admin"
    })
	@Transactional
    public Account select(@PathParam("id") long id) {
		return accounts.findOne(id);
    }
	
	@GET
	@Path("/{id}/full")
	@RolesAllowed({
        "res:///accounts:admin"
    })
	@CommentsView
	@TagsView
	@AttachmentsView
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Account selectFull(@PathParam("id") long id) {
		return accounts.findOne(id);
    }
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({
        "res:///accounts:admin"
    })
	@Transactional
    public Account create(Account account) {
	
	    int random = security.randomInt();
	    account.setRandom(random);
	    account.setPwdhash(security.hashPassword(random, account.getPassword()));
		
		Account p = (Account) service.create(null, account, "accounts");
		em.clear();
		return accounts.findOne(p.getId());
    }
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({
        "res:///accounts:admin"
    })
	@Transactional
    public Account update(Account account) {
		
	    int random = security.randomInt();
	    account.setRandom(random);
	    account.setPwdhash(security.hashPassword(random, account.getPassword()));
		
		Account p = (Account) service.update(account);
		em.clear();
		return accounts.findOne(p.getId());
    }
	
	@DELETE
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@RolesAllowed({
        "res:///accounts:admin"
    })
	@Transactional
    public Account remove(@PathParam("id") long id) {
		
		Account p = accounts.findOne(id);
		service.remove(p);
		em.clear();
		return p;
    }
}
