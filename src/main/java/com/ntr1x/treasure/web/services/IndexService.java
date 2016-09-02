package com.ntr1x.treasure.web.services;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.events.ResourceEvent;
import com.ntr1x.treasure.web.index.PublicationIndex;
import com.ntr1x.treasure.web.index.PublicationIndexRepository;
import com.ntr1x.treasure.web.model.Publication;
import com.ntr1x.treasure.web.services.ISubscriptionService.ResourceMessage;

@Component
@Scope("singleton")
public class IndexService implements IIndexService {
    
    @Inject
    private PublicationIndexRepository publications;
    
    @PostConstruct
    public void init() {
        System.out.println(this);
    }
    
    @EventListener
    public void handle(ResourceEvent event) {
        
        ResourceMessage source = event.getSource();
        
        switch(source.type) {
        case CREATE:
        case UPDATE:
            handleSave(source.object);
            break;
        case REMOVE:
            handleRemove(source.object);
            break;
        }
    }
    
    private void handleSave(Object object) {
        if (object instanceof Publication) {
            handleSavePublication((Publication) object);
        }
    }

    private void handleRemove(Object object) {
        if (object instanceof Publication) {
            handleRemovePublication((Publication) object);
        }
    }

    private void handleSavePublication(Publication p) {
        
        
        publications.save(
            new PublicationIndex(
                p.getAlias(),
                p.getId(),
                "publication",
                p.getTitle(),
                p.getPromo(),
                p.getContent(),
                p.getTags() == null ? null : p.getTags().stream().map(t -> t.getValue()).toArray(String[]::new),
                p.getCategories() == null ? null : p.getCategories().stream().map(c -> c.getCategory().getId()).toArray(Long[]::new)
            )
        );
    }
    
    private void handleRemovePublication(Publication p) {
        
        publications.delete(p.getId());
    }
}
