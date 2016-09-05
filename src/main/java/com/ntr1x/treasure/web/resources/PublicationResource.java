//package com.ntr1x.treasure.web.resources;
//
//import java.time.LocalDateTime;
//import java.util.Collections;
//import java.util.List;
//
//import javax.annotation.security.PermitAll;
//import javax.annotation.security.RolesAllowed;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.transaction.Transactional;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.core.MediaType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
//
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.support.TransactionSynchronizationAdapter;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
//import com.ntr1x.treasure.web.converter.AppConverterProvider.LocalDateTimeConverter;
//import com.ntr1x.treasure.web.events.ResourceEvent;
//import com.ntr1x.treasure.web.index.PublicationIndex;
//import com.ntr1x.treasure.web.index.PublicationIndexRepository;
//import com.ntr1x.treasure.web.model.Action;
//import com.ntr1x.treasure.web.model.Category;
//import com.ntr1x.treasure.web.model.Publication;
//import com.ntr1x.treasure.web.model.ResourceCategory;
//import com.ntr1x.treasure.web.model.Tag;
//import com.ntr1x.treasure.web.reflection.ResourceUtils;
//import com.ntr1x.treasure.web.repository.PublicationRepository;
//import com.ntr1x.treasure.web.services.IPublisherSevice;
//import com.ntr1x.treasure.web.services.ISubscriptionService.ResourceMessage;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiParam;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//@Path("publications")
//@Api("Publications")
//@PermitAll
//@Component
//public class PublicationResource {
//	
//	@Inject
//	private EntityManager em;
//	
//	@Inject
//	private PublicationRepository publications;
//	
//	@Inject
//	private IPublisherSevice publisher;
//	
//	@Inject
//    private PublicationIndexRepository index;
//	
//	@GET
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public List<Publication> list(
//	        @QueryParam("category") List<Long> categories,
//			@QueryParam("page") @ApiParam(example = "0") int page,
//			@QueryParam("size") @ApiParam(example = "10") int size
//	) {
//	    return publications.findAll(new PageRequest(0, size)).getContent();
//	}
//	
//	@GET
//	@Path("/query")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public List<Publication> query(
//            @QueryParam("query") String query,
//            @QueryParam("category") List<Long> categories,
//            @QueryParam("page") @ApiParam(example = "0") int page,
//            @QueryParam("size") @ApiParam(example = "10") int size
//    ) {
//	    
//	    List<PublicationIndex> result = index.search(
//            query,
//            categories
//	    );
//	    
//	    Long[] identifiers = result
//            .stream()
//            .map(p -> p.resource)
//            .toArray(Long[]::new)
//        ;
//	    
//	    return identifiers.length > 0
//            ? publications.findByIdIn(identifiers)
//            : Collections.emptyList()
//        ;
//    }
//	
//	@GET
//	@Path("/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	public Publication select(@PathParam("id") long id) {
//	    
//	    return publications.findOne(id);
//	}
//	
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	@RolesAllowed({ "res:///publications:admin" })
//	public Publication create(PublicationCreate publication) {
//
//	    Publication persisted = new Publication(); {
//	        
//	        persisted.setTitle(publication.title);
//	        persisted.setPromo(publication.promo);
//	        persisted.setContent(publication.content);
//            
//	        em.persist(persisted);
//	        em.flush();
//	        
//	        persisted.setAlias(ResourceUtils.alias(null, "publications", persisted));
//            
//            em.merge(persisted);
//            em.flush();
//            
//            if (publication.tags != null) {
//                for (PublicationCreate.Tag t : publication.tags) {
//                    
//                    Tag tag = new Tag(); {
//                        
//                        tag.setRelate(persisted);
//                        tag.setValue(t.value);
//                        
//                        em.persist(tag);
//                        em.flush();
//                        
//                        tag.setAlias(ResourceUtils.alias(persisted, "tags", tag));
//                        
//                        em.merge(persisted);
//                        em.flush();
//                    }
//                }
//            }
//            
//            if (publication.categories != null) {
//                for (PublicationCreate.Category c : publication.categories) {
//                    
//                    ResourceCategory category = new ResourceCategory(); {
//                        
//                        category.setRelate(persisted);
//                        category.setCategory(em.find(Category.class, c.category));
//                        
//                        em.persist(category);
//                        em.flush();
//                        
//                        category.setAlias(ResourceUtils.alias(persisted, "categories", category));
//                        
//                        em.merge(persisted);
//                        em.flush();
//                    }
//                }
//            }
//	    }
//	    
//	    em.refresh(persisted);
//	    
//	    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
//	        
//	        @Override
//	        public void afterCommit() {
//	            
//	            publisher.publishEvent(
//                    new ResourceEvent(
//                        new ResourceMessage(
//                            persisted.getAlias(),
//                            ResourceMessage.Type.CREATE,
//                            persisted
//                        )
//                    )
//                );
//	        }
//	    });
//		
//		return persisted;
//	}
//	
//	@PUT
//	@Path("/i/{id}")
//	@Consumes(MediaType.APPLICATION_JSON)
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	@RolesAllowed({ "res:///publications/i/{id}:admin" })
//	public Publication update(@PathParam("id") long id, PublicationUpdate publication) {
//	    
//		Publication persisted = em.find(Publication.class, id); {
//		    
//		    persisted.setTitle(publication.title);
//		    persisted.setPromo(publication.promo);
//		    persisted.setContent(publication.content);
//		    
//		    em.merge(persisted);
//            em.flush();
//		}
//		
//		return persisted;
//	}
//	
//	@DELETE
//	@Path("/i/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@Transactional
//	@RolesAllowed({ "res:///publications/{id}:admin" })
//	public Publication remove(@PathParam("id") long id) {
//		
//		Publication persisted = em.find(Publication.class, id); {
//		    
//		    em.remove(persisted);
//	        em.flush();
//		}
//		
//		return persisted;
//	}
//	
//	@XmlRootElement
//	@NoArgsConstructor
//	@AllArgsConstructor
//    public static class PublicationCreate {
//        
//        public String title;
//        public String promo;
//        public String content;
//        
//        @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
//        public LocalDateTime published;
//        
//        @XmlElement
//        public Tag[] tags;
//        
//        @XmlElement
//        public Category[] categories;
//        
//        @XmlElement
//        public Attachment[] attachments;
//        
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Tag {
//            
//            public String value;
//        }
//        
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Category {
//
//            public Long category;
//        }
//        
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Attachment {
//            
//            public Long upload;
//        }
//    }
//    
//    @XmlRootElement
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class PublicationUpdate {
//        
//        public String title;
//        public String promo;
//        public String content;
//        
//        @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
//        public LocalDateTime published;
//        
//        @XmlElement
//        public Tag[] tags;
//
//        @XmlElement
//        public Category[] categories;
//        
//        @XmlElement
//        public Attachment[] attachments;
//        
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Tag {
//            
//            public Long id;
//            public String value;
//            public Action _action;
//        }
//        
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Category {
//            
//            public Long id;
//            public Long category;
//            public Action _action;
//        }
//        
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Attachment {
//            
//            public Long id;
//            public Long upload;
//            public Action _action;
//        }
//    }
//}
