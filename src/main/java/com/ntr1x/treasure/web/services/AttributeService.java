package com.ntr1x.treasure.web.services;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p1.Attribute;
import com.ntr1x.treasure.web.model.p2.AttributeOption;
import com.ntr1x.treasure.web.model.p2.ResourceAttribute;
import com.ntr1x.treasure.web.reflection.ResourceUtils;
import com.ntr1x.treasure.web.repository.AttributeRepository;

@Service
public class AttributeService implements IAttributeService {

    @Inject
    private EntityManager em;
    
    @Inject
    private ISecurityService security;
    
    @Inject
    private AttributeRepository attributes;

    @Override
    public Attribute create(AttributeCreate create) {
        
        Attribute attribute = new Attribute(); {
            
            attribute.setTitle(create.title);
            attribute.setFilter(create.filter);
            attribute.setOrder(create.order);
            attribute.setAspects(create.aspects == null ? null : Arrays.asList(create.aspects));
            
            em.persist(attribute);
            em.flush();
        }
        
        security.register(attribute, ResourceUtils.alias(null, "attributes/i", attribute));
        
        this.createOptions(attribute, create.options);
        
        em.refresh(attribute);
        
        return attribute;
    }
    
    @Override
    public Attribute update(long id, AttributeUpdate update) {
        
        Attribute attribute = em.find(Attribute.class, id); {
            
            attribute.setTitle(update.title);
            attribute.setFilter(update.filter);
            attribute.setOrder(update.order);
            attribute.setAspects(update.aspects == null ? null : Arrays.asList(update.aspects));
            
            em.merge(attribute);
            em.flush();
        }
        
        this.updateOptions(attribute, update.options);
        
        em.refresh(attribute);
        
        return attribute;
    }
    
    @Override
    public Attribute remove(long id) {
        
        Attribute attribute = em.find(Attribute.class, id); {
        
            em.remove(attribute);
            em.flush();
        }
        
        return attribute;
    }
    
    @Override
    public Attribute select(long id) {
        
        Attribute attribute = em.find(Attribute.class, id);
        return attribute;
    }
    
    @Override
    public AttributesResponse list(int page, int size, List<String> aspects) {
        
        Page<Attribute> p = aspects == null || aspects.isEmpty()
            ? attributes.findAll(new PageRequest(page, size))
            : attributes.findByAspects(aspects, new PageRequest(page, size))
        ;
        
        return new AttributesResponse(
            p.getTotalElements(),
            page,
            size,
            p.getContent()
        );
    }
    
    @Override
    public void createAttributes(Resource resource, RelatedAttribute[] attributes) {
        
        if (attributes != null) {
            
            for (RelatedAttribute p : attributes) {
                
                ResourceAttribute v = new ResourceAttribute(); {
                    
                    Attribute e = em.find(Attribute.class, p.attribute);
                    
                    v.setRelate(resource);
                    v.setValue(p.value);
                    v.setAttribute(e);
                    
                    em.persist(v);
                }
            }
            
            em.flush();
        }
    }
    
    @Override
    public void updateAttributes(Resource resource, RelatedAttribute[] attributes) {
        
        if (attributes != null) {
            
            for (RelatedAttribute p : attributes) {
                
                switch (p.action) {
                
                    case CREATE: {
                        
                        ResourceAttribute v = new ResourceAttribute(); {
                            
                            Attribute e = em.find(Attribute.class, p.attribute);
                            
                            v.setRelate(resource);
                            v.setValue(p.value);
                            v.setAttribute(e);
                            
                            em.persist(v);
                        }
                        break;
                    }
                    case UPDATE: {
                        
                        ResourceAttribute v = em.find(ResourceAttribute.class, p.id); {
                            
                          v.setValue(p.value);
                          
                          em.merge(v);
                        }
                        
                        break;
                    }
                    case REMOVE: {
                        
                        ResourceAttribute v = em.find(ResourceAttribute.class, p.id); {
                            em.remove(v);
                        }
                        break;
                    }
                default:
                    break;
                }
            }
            
            em.flush();
        }
    }
    
    @Override
    public void createOptions(Attribute attribute, RelatedOption[] options) {
        
        if (options != null) {
            
            for (RelatedOption p : options) {
                
                AttributeOption v = new AttributeOption(); {
                    
                    v.setAttribute(attribute);
                    v.setTitle(p.title);
                    v.setValue(p.value);
                    
                    em.persist(v);
                }
            }
            
            em.flush();
        }
    }
    
    @Override
    public void updateOptions(Attribute attribute, RelatedOption[] options) {
        
        if (options != null) {
            
            for (RelatedOption p : options) {
                
                switch (p.action) {
                
                    case CREATE: {
                        
                        AttributeOption v = new AttributeOption(); {
                            
                            v.setAttribute(attribute);
                            v.setTitle(p.title);
                            v.setValue(p.value);
                            
                            em.persist(v);
                        }
                        break;
                    }
                    case UPDATE: {
                        
                        AttributeOption v = em.find(AttributeOption.class, p.id); {
                            
                            v.setAttribute(attribute);
                            v.setTitle(p.title);
                            v.setValue(p.value);
                            
                            em.merge(v);
                        }
                        break;
                    }
                    case REMOVE: {
                        
                        AttributeOption v = em.find(AttributeOption.class, p.id); {
                            em.remove(v);
                        }
                        break;
                    }
                }
            }
            
            em.flush();
        }
    }
}
