package com.ntr1x.treasure.web.bootstrap;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.ntr1x.treasure.web.model.Publication;
import com.ntr1x.treasure.web.resources.PublicationResource.PublicationCreate;

public class BootstrapPublications {
    
    private Bootstrap bootstrap;

    public BootstrapPublications(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }
    
    public List<Publication> createPublications(WebTarget target, String token) {
        
        List<Publication> publications = new ArrayList<>();
        List<PublicationCreate> create = new ArrayList<>();
        
        {
            PublicationCreate p = new PublicationCreate(); {
                
                p.title = "Первый пример";
                p.promo = "Короткий текст примера";
                p.content = "Полный текст примера";
                p.published = LocalDateTime.now();
                p.categories = new PublicationCreate.Category[] {
                    new PublicationCreate.Category(bootstrap.directories.conference.getId()),
                    new PublicationCreate.Category(bootstrap.specializations.arthroscopy.getId()),
                };
            }
            
            create.add(p);
        }
        
        {
            PublicationCreate p = new PublicationCreate(); {
                
                p.title = "Второй пример";
                p.promo = "Короткий текст второго примера";
                p.content = "Полный текст второго примера";
                p.published = LocalDateTime.now();
                p.categories = new PublicationCreate.Category[] {
                    new PublicationCreate.Category(bootstrap.directories.expose.getId()),
                    new PublicationCreate.Category(bootstrap.specializations.specialization.getId()),
                    new PublicationCreate.Category(bootstrap.localizations.hip.getId()),
                };
            }
            
            create.add(p);
        }
        
        for (PublicationCreate p : create) {
            
            Publication r = target
                .path("/publications")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(p, MediaType.APPLICATION_JSON_TYPE), Publication.class)
            ;
            
            publications.add(r);
        }
        
        return publications;
    }
}
