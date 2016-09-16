package com.ntr1x.treasure.web.bootstrap;

import javax.inject.Inject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.p1.Attribute;
import com.ntr1x.treasure.web.services.IAttributeService;
import com.ntr1x.treasure.web.services.IAttributeService.AttributeCreate;

@Service
public class BootstrapAttributes {
    
    @Inject
    private BootstrapHolder holder;
    
    public BootstrapState.Attributes createAttributes(WebTarget target) {
        
        BootstrapState state = holder.get();
        
        BootstrapState.Attributes attributes = new BootstrapState.Attributes();
        
        {
            AttributeCreate s = new AttributeCreate(); {
                
                s.title = "Бренд";
                s.order = 1;
                s.filter = true;
                s.aspects = new String[] { "good" };
            }
            
            attributes.goodBrand = target
                .path("/attributes")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.admin)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Attribute.class)
            ;
        }
        
        {
            AttributeCreate s = new AttributeCreate(); {
                
                s.title = "Цвет";
                s.order = 2;
                s.filter = false;
                s.aspects = new String[] { "modification" };
                
                s.options = new IAttributeService.RelatedOption[] {
                    new IAttributeService.RelatedOption(null, "Белый", "#FFFFFF", null),
                    new IAttributeService.RelatedOption(null, "Чёрный", "#000000", null),
                    new IAttributeService.RelatedOption(null, "Красный", "#FF0000", null),
                    new IAttributeService.RelatedOption(null, "Зелёный", "#00FF00", null),
                    new IAttributeService.RelatedOption(null, "Синий", "#0000FF", null),
                };
            }
            
            attributes.modificationColor = target
                .path("/attributes")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.admin)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Attribute.class)
            ;
        }
        
        {
            AttributeCreate s = new AttributeCreate(); {
                
                s.title = "Размер";
                s.order = 3;
                s.filter = false;
                s.aspects = new String[] { "modification" };
                
                s.options = new IAttributeService.RelatedOption[] {
                    new IAttributeService.RelatedOption(null, "S", "S", null),
                    new IAttributeService.RelatedOption(null, "M", "M", null),
                    new IAttributeService.RelatedOption(null, "L", "L", null),
                    new IAttributeService.RelatedOption(null, "XL", "XL", null),
                    new IAttributeService.RelatedOption(null, "XXL", "XXL", null),
                    new IAttributeService.RelatedOption(null, "XXXL", "XXXL", null),
                };
            }
            
            attributes.modificationSize = target
                .path("/attributes")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, state.sessions.admin)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Attribute.class)
            ;
        }
        
        return attributes;
    }
}
