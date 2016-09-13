package com.ntr1x.treasure.web.resources;

import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Comment;
import com.ntr1x.treasure.web.model.Grant;
import com.ntr1x.treasure.web.model.Like;
import com.ntr1x.treasure.web.model.Resource;
import com.ntr1x.treasure.web.model.Session;
import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.LikeRepository;
import com.ntr1x.treasure.web.repository.ResourceRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Path("resources")
@Api("Resources")
@Component
public class ResourceResource {
	
    @PersistenceContext
    private EntityManager em;
    
	@Inject
	private ResourceRepository resources;
	
	@Inject
	private LikeRepository likes;
	
	@Inject
	private Session session;
	
	
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
				? resources.findOrderByAlias(new PageRequest(page, size))
				: resources.findByAliasLikeOrderByAlias(pattern, new PageRequest(page, size))
		).getContent();
    }
	
	@GET
	@Path("/i/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Resource select(@PathParam("id") long id) {
		return resources.findOne(id);
    }
	
	@GET
	@Path("/a/{alias}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
    public Resource select(@PathParam("alias") String alias) {
		return resources.findByAlias(alias);
    }
	
	@POST
    @Path("/i/{id}/likes")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public Like like(@PathParam("id") long id, LikeRequest like) {
	    
	    if (like.value > 1 || like.value < -1) {
	        throw new WebApplicationException("Wrong rate value", Response.Status.BAD_REQUEST);
	    }
	    
	    User u = session.getUser();
	    Resource r = em.find(Resource.class, id);
	    
	    Like l = likes.findByRelateIdAndUserId(id, session.getUser().getId());
	    
	    if (l != null) {
	        
	        int v = l.getValue() + like.value;
	        if (v == 0) {
	            em.remove(l);
	        } else {
	            l.setValue(like.value);
	            em.merge(l);
	        }
	        
	    } else {
	        
	        l = new Like(); {

	            l.setValue(like.value);
	            l.setUser(u);
	            l.setRelate(r);
	            
	            em.persist(l);
	            em.flush();
	            
	            l.setAlias(ResourceUtils.alias(r, "likes", l));
	            
	            em.merge(l);
	        }
	    }
	    
	    return l;
    }
	
	@POST
    @Path("/i/{id}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "auth" })
    @Transactional
    public Comment commentCreate(@PathParam("id") long id, CommentRequest request) {
        
        User u = session.getUser();
        Resource r = em.find(Resource.class, id);
        
        Comment c = new Comment(); {
            c.setRelate(r);
            c.setUser(u);
            c.setModerated(false);
            c.setMessage(request.message);
            c.setPublished(LocalDateTime.now());
            c.setConfidential(request.confidential);
        }
        
        em.persist(c);
        em.flush();
        
        c.setAlias(String.format("/resources/i/%d/comments/i/%d", id, c.getId()));
        
        em.merge(c);
        em.flush();
        
        Grant g = new Grant(); {
            
            g.setAlias(c.getAlias());
            g.setUser(u);
            g.setAction("admin");
            
            em.persist(g);
            em.flush();
            
            g.setAlias(ResourceUtils.alias(u, "grants/i", g));
            
            em.merge(g);
            em.flush();
        }
        
        return c;
    }
	
	@PUT
    @Path("/i/{id}/comments/i/{comment}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "/i/{id}/comments/i/{comment}:admin" })
    @Transactional
    public Comment commentUpdate(@PathParam("id") long id, @PathParam("comment") long comment, CommentRequest request) {
	    
        Resource r = em.find(Resource.class, id);
        
        Comment c = em.find(Comment.class, comment); {
            
            if (c.getRelate().getId() != r.getId()) {
                throw new WebApplicationException("Comment relates to another resource", Response.Status.CONFLICT);
            }
            
            c.setModerated(false);
            c.setMessage(request.message);
            c.setConfidential(request.confidential);
        }
        
        em.merge(c);
        em.flush();
        
        return c;
    }
	
	@DELETE
    @Path("/i/{id}/comments/i/{comment}")
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({ "/i/{id}/comments/i/{comment}:admin" })
    @Transactional
    public Comment commentRemove(@PathParam("id") long id, @PathParam("comment") long comment) {
        
        Resource r = em.find(Resource.class, id);
        
        Comment c = em.find(Comment.class, comment); {
            
            if (c.getRelate().getId() != r.getId()) {
                throw new WebApplicationException("Comment relates to another resource", Response.Status.CONFLICT);
            }
        }
        
        em.remove(c);
        em.flush();
        
        return c;
    }
	
	@XmlRootElement
	@NoArgsConstructor
	@AllArgsConstructor
	public static class LikeRequest {
	    
	    public int value;
	    public String message;
	}
	
	@XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CommentRequest {
	    
	    public boolean confidential;
        public String message;
    }
}
