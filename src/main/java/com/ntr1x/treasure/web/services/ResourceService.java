package com.ntr1x.treasure.web.services;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ntr1x.treasure.web.model.Resource;
import com.ntr1x.treasure.web.reflection.ManagedDescriptor;
import com.ntr1x.treasure.web.reflection.ResourceProperty;
import com.ntr1x.treasure.web.reflection.ResourceUtils;

@Service
public class ResourceService {

    @Inject
    private EntityManager em;

    public EntityManager getEntityManager() {
        return this.em;
    }
    
    public Resource create(Resource parent, Resource managed, String locator) {
        
        Assert.isTrue(managed.getId() == null);

        ManagedDescriptor[] ignore = ResourceUtils.values(managed, ResourceProperty.Type.IGNORE, null);
        // ManagedDescriptor[] assign = ManagedUtils.values(managed, ManagedProperty.Type.ASSIGN, null);
        ManagedDescriptor[] cascade = ResourceUtils.values(managed, ResourceProperty.Type.CASCADE, null);
        
        // evacuate & reset cascaded properties
        Resource cascadeCopy = BeanUtils.instantiate(managed.getClass());
        for (ManagedDescriptor d : cascade) {
            Object value = d.get.apply(managed);
            d.set.accept(cascadeCopy, value);
            d.set.accept(managed, null);
        }
        
        // reset ignored properties
        for (ManagedDescriptor d : ignore) {
            d.set.accept(managed, null);
        }
        
        em.persist(managed);
        em.flush();
        
        managed.setAlias(parent != null
            ? String.format("%s/%s/%d", ((Resource) parent).getAlias(), locator, managed.getId())
            : String.format("/%s/%d", locator, managed.getId())
        );
        
        em.merge(managed);
        em.flush();
        
        // restore cascaded properties
        for (ManagedDescriptor d : cascade) {
            Object value = d.get.apply(cascadeCopy);
//            d.set.accept(managed, value);
            handleRelatedChildren(managed, value, d);
        }
        
        em.refresh(managed);
        return managed;
    }
    
    public Resource update(Resource managed) {
        
        Assert.isTrue(managed.getId() == null);
        
        ManagedDescriptor[] ignore = ResourceUtils.values(managed, null, ResourceProperty.Type.IGNORE);
//        ManagedDescriptor[] assign = ManagedUtils.values(managed, null, ManagedProperty.Type.ASSIGN);
        ManagedDescriptor[] cascade = ResourceUtils.values(managed, null, ResourceProperty.Type.CASCADE);
        
        // evacuate & reset cascaded properties
        Resource cascadeCopy = BeanUtils.instantiate(managed.getClass());
        for (ManagedDescriptor d : cascade) {
            Object value = d.get.apply(managed);
            d.set.accept(cascadeCopy, value);
            d.set.accept(managed, null);
        }
//        
        // reset ignored properties
        for (ManagedDescriptor d : ignore) {
            d.set.accept(managed, null);
        }
        
        em.merge(managed);
        em.flush();
        
        // restore cascaded properties
        for (ManagedDescriptor d : cascade) {
            Object value = d.get.apply(cascadeCopy);
//            d.set.accept(managed, value);
            handleRelatedChildren(managed, value, d);
        }
        
        em.refresh(managed);
        return managed;
    }
    
    public final Resource remove(Resource resource) {
        
        Assert.isTrue(resource.getId() != null);
        
        em.remove(resource);
        em.flush();
        
        return resource;
    }
    
    private void handleRelatedChildren(Resource parent, Object children, ManagedDescriptor md) {
        
        if (children != null) {
            
            if (children instanceof Iterable<?>) {
            
                for (Object child : (Iterable<?>) children) {
                    
                    Assert.isTrue(child instanceof Resource);
                    Resource r = (Resource) child;
                    
                    switch (r.getAction()) {
                    case CREATE:
                        create(parent, r, md.alias);
                        break;
                    case UPDATE:
                        update(r);
                        break;
                    case REMOVE:
                        remove(r);
                        break;
                    case IGNORE:
                        break;
                    }
                }
            }
        }
    }
}
