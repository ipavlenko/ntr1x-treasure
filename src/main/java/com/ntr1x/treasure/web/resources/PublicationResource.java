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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.ntr1x.treasure.web.converter.AppConverterProvider.LocalDateTimeConverter;
import com.ntr1x.treasure.web.events.ResourceEvent;
import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.Publication;
import com.ntr1x.treasure.web.repository.PublicationRepository;
import com.ntr1x.treasure.web.services.IPublisherSevice;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;

@Path("publications")
@Api("Publications")
@PermitAll
@Component
public class PublicationResource {
	
	@Inject
	private EntityManager em;
	
	@Inject
	private PublicationRepository publications;
	
	@Inject
	private IPublisherSevice publisher;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public List<Publication> list(
			@QueryParam("page") @ApiParam(example = "0") int page,
			@QueryParam("size") @ApiParam(example = "10") int size
	) {
	    return publications.findAll(new PageRequest(0, size)).getContent();
	}
	
	@GET
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Publication select(@PathParam("id") long id) {
	    
	    return publications.findOne(id);
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "/publications:admin" })
	public Publication create(PublicationCreate publication) {

	    Publication persisted = new Publication(); {
	        
	        persisted.setTitle(publication.title);
	        persisted.setPromo(publication.promo);
	        persisted.setContent(publication.content);
            
	        em.persist(persisted);
	        em.flush();
	    }
	    
	    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
	        
	        @Override
	        public void afterCommit() {
	            
	            publisher.publishEvent(new ResourceEvent.CREATED(persisted));
	        }
	    });
		
		return persisted;
	}
	
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "/publications/{id}:admin" })
	public Publication update(@PathParam("id") long id, PublicationUpdate publication) {
	    
		Publication persisted = em.find(Publication.class, id); {
		    
		    persisted.setTitle(publication.title);
		    persisted.setPromo(publication.promo);
		    persisted.setContent(publication.content);
		    
		    em.merge(persisted);
		    em.flush();
		}
		
		return persisted;
	}
	
	@DELETE
	@Path("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	@RolesAllowed({ "/publications/{id}:admin" })
	public Publication remove(@PathParam("id") long id) {
		
		Publication persisted = em.find(Publication.class, id); {
		    
		    em.remove(persisted);
	        em.flush();
		}
		
		return persisted;
	}
	
	@XmlRootElement
    public static class PublicationCreate {
        
        public String title;
        public String promo;
        public String content;
        
        @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
        public String published;
        
        @XmlElement
        public List<Tag> tags;
        
        public static class Tag {
            
            public String value;
        }
    }
    
    @XmlRootElement
    public static class PublicationUpdate {
        
        public String title;
        public String promo;
        public String content;
        
        @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
        public String published;
        
        @XmlElement
        public List<Tag> tags;
        
        public static class Tag {
            
            public Long id;
            public String value;
            public Action _action;
        }
    }
}
