package com.ntr1x.treasure.web.resources;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

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

import com.google.common.hash.Hashing;
import com.ntr1x.treasure.web.model.Account;
import com.ntr1x.treasure.web.model.Resource.AttachmentsView;
import com.ntr1x.treasure.web.model.Resource.CommentsView;
import com.ntr1x.treasure.web.model.Resource.TagsView;
import com.ntr1x.treasure.web.repository.AccountRepository;
import com.ntr1x.treasure.web.services.ResourceService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Path("accounts")
@Api("Accounts")
@Component
public class AccountResource {
	
	@Inject
	private EntityManager em;
	
	@Inject
	private ResourceService service;
	
	@Inject
	private AccountRepository accounts;
	
	private Random random = new Random();
	
	@GET
    @Produces(MediaType.APPLICATION_JSON)
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
	@Transactional
    public Account select(@PathParam("id") long id) {
		return accounts.findOne(id);
    }
	
	@GET
	@Path("/{id}/full")
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
	@Transactional
    public Account create(Account post) {
	
		post.setRandom(random.nextInt());
		post.setPwdhash(
			Hashing.md5()
				.newHasher()
				.putLong(post.getRandom())
				.putString(post.getPassword(), Charset.forName("UTF-8"))
				.hash()
				.toString()
		);
		
		Account p = service.create(post, (r) -> String.format("/accounts/%d", r.getId()), (r) -> r.getId() == null);
		em.clear();
		return accounts.findOne(p.getId());
    }
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Account update(Account post) {
		
		post.setRandom(random.nextInt());
		post.setPwdhash(
			Hashing.md5()
				.newHasher()
				.putLong(post.getRandom())
				.putString(post.getPassword(), Charset.forName("UTF-8"))
				.hash()
				.toString()
		);
		
		Account p = service.update(post, (r) -> r.getId() != null);
		em.clear();
		return accounts.findOne(p.getId());
    }
	
	@DELETE
	@Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Account remove(@PathParam("id") long id) {
		
		Account p = accounts.findOne(id);
		service.remove(p, (r) -> r.getId() != null);
		em.clear();
		return p;
    }
}
