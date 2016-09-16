package com.ntr1x.treasure.web.services;

import javax.inject.Inject;

import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.events.ResourceEvent;
import com.ntr1x.treasure.web.index.ModificationIndex;
import com.ntr1x.treasure.web.index.ModificationIndexRepository;
import com.ntr1x.treasure.web.model.p3.Good;
import com.ntr1x.treasure.web.model.p4.Modification;
import com.ntr1x.treasure.web.services.ISubscriptionService.ResourceMessage;

@Component
@Scope("singleton")
public class IndexService implements IIndexService {
    
    @Inject
    private ModificationIndexRepository modifications;
    
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
        if (object instanceof Modification) {
            handleSaveModification((Modification) object);
        }
    }

    private void handleRemove(Object object) {
        if (object instanceof Modification) {
            handleRemoveModification((Modification) object);
        }
    }

    private void handleSaveModification(Modification m) {
        
        Good g = m.getGood();
        
        String[] tags = g.getTags() == null
            ? null
            : g.getTags()
                .stream()
                .map(t -> t.getValue()).toArray(String[]::new)
        ;
        
        String[] attributes = g.getAttributes() == null
            ? null
            : g.getAttributes()
                .stream()
                .map(a -> String.format("attribute$%d$%s", a.getAttribute().getId(), a.getValue())).toArray(String[]::new)
        ;
        
        Long[] categories = g.getCategories() == null
            ? null
            : g.getCategories()
                .stream()
                .map(c -> c.getCategory().getId()).toArray(Long[]::new)
        ;
        
        modifications.save(
            new ModificationIndex(
                m.getAlias(),
                m.getId(),
                m.getGood().getId(),
                m.getGood().getPurchase().getId(),
                "modification",
                m.getGood().getTitle(),
                m.getGood().getPromo(),
                "",
                tags,
                categories,
                attributes
            )
        );
    }
    
    private void handleRemoveModification(Modification m) {
        
        modifications.delete(m.getId());
    }
}
