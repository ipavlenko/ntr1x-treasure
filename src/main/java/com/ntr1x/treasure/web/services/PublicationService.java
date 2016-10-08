package com.ntr1x.treasure.web.services;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.events.ResourceEvent;
import com.ntr1x.treasure.web.index.PublicationIndexRepository;
import com.ntr1x.treasure.web.index.PublicationIndexRepositoryCustom.SearchRequest;
import com.ntr1x.treasure.web.index.PublicationIndexRepositoryCustom.SearchResult;
import com.ntr1x.treasure.web.model.p3.Publication;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.PublicationRepository;
import com.ntr1x.treasure.web.services.IPublicationService.PublicationsResponse.PublicationItem;
import com.ntr1x.treasure.web.services.ISubscriptionService.ResourceMessage;

@Service
public class PublicationService implements IPublicationService {

    @Inject
    private EntityManager em;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private IPublisherSevice publisher;
    
    @Inject
    private ITransactionService transactions;
    
    @Inject
    private PublicationRepository publications;
    
    @Inject
    private PublicationIndexRepository index;
    
    @Inject
    private ITagService tags;
    
    @Inject
    private IImageService images;
    
    @Inject
    private ICategoryService categories;
    
    @Override
    public PublicationsResponse list(int page, int size) {
        
        Page<Publication> result = publications.findAll(new PageRequest(page, size));
        
        return new PublicationsResponse(
            result.getTotalElements(),
            page,
            size,
            result.getContent().stream().map((p) ->
                new PublicationItem(
                    p,
                    p.getTags(),
                    p.getImages(),
                    p.getCategories()
                )
            ).collect(Collectors.toList())
        );
    }
    
    @Override
    public PublicationsResponse search(int page, int size, String query, LocalDateTime since, LocalDateTime until, Long[][] categories) {

        SearchResult result = index.search(
            new SearchRequest(
                page,
                size,
                query,
                since,
                until,
                categories
            )
        );
        
        List<Publication> publications = result.items.size() > 0
            ? this.publications.findByIdIn(result.items.toArray(new Long[0]))
            : Collections.emptyList()
        ;
        
        return new PublicationsResponse(
            result.count,
            page,
            size,
            publications.stream().map((p) ->
                new PublicationItem(
                    p,
                    p.getTags(),
                    p.getImages(),
                    p.getCategories()
                )
            ).collect(Collectors.toList())
        );
    }
    
    @Override
    public Publication create(PublicationCreate request) {
        
        Publication persisted = new Publication(); {
            
            persisted.setTitle(request.title);
            persisted.setPromo(request.promo);
            persisted.setContent(request.content);
            persisted.setPublished(request.published);
            
            em.persist(persisted);
            em.flush();
            
            security.register(persisted, ResourceUtils.alias(null, "publications/i", persisted));
            
            tags.createTags(persisted, request.tags);
            images.createImages(persisted, request.images);
            categories.createCategories(persisted, request.categories);
        }
        
        em.refresh(persisted);

        transactions.afterCommit(() -> {

            publisher.publishEvent(
                new ResourceEvent(
                    new ResourceMessage(
                        persisted.getAlias(),
                        ResourceMessage.Type.CREATE,
                        persisted
                    )
                )
            );
        });

        return persisted;
    }

    @Override
    public Publication update(long id, PublicationUpdate request) {
        
        Publication persisted = em.find(Publication.class, id); {
            
            persisted.setTitle(request.title);
            persisted.setPromo(request.promo);
            persisted.setContent(request.content);
            persisted.setPublished(request.published);
            
            em.merge(persisted);
            em.flush();
            
            tags.updateTags(persisted, request.tags);
            images.updateImages(persisted, request.images);
            categories.updateCategories(persisted, request.categories);
        }

        em.refresh(persisted);

        transactions.afterCommit(() -> {
            
            publisher.publishEvent(
                new ResourceEvent(
                    new ResourceMessage(
                        persisted.getAlias(),
                        ResourceMessage.Type.UPDATE,
                        persisted
                    )
                )
            );
        });

        return persisted;
    }

    @Override
    public Publication remove(long id) {
        
        Publication persisted = em.find(Publication.class, id); {
            
            em.remove(persisted);
            em.flush();
        }
        
        em.refresh(persisted);

        transactions.afterCommit(() -> {

            publisher.publishEvent(
                new ResourceEvent(
                    new ResourceMessage(
                        persisted.getAlias(),
                        ResourceMessage.Type.REMOVE,
                        persisted
                    )
                )
            );
        });

        return persisted;
    }

    @Override
    public PublicationItem select(long id) {
        
        Publication p = em.find(Publication.class, id);
        
        return new PublicationItem(
            p,
            p.getTags(),
            p.getImages(),
            p.getCategories()
        );
    }
}
