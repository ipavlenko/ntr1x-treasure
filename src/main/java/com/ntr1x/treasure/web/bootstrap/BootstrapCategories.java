package com.ntr1x.treasure.web.bootstrap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.bootstrap.BootstrapState.Directories;
import com.ntr1x.treasure.web.model.Category;
import com.ntr1x.treasure.web.resources.CategoryResource.CategoryCreate;

@Service
public class BootstrapCategories {
    
    public Directories createDirectories(WebTarget target, String token) {
        
        Directories directories = new Directories();
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Одежда для взрослых";
                s.aspects = new String[] { "directory" };
            }
            
            directories.adult = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Белье и одежда  для мужчин";
                s.relate = directories.adult.getId();
                s.aspects = new String[] { "directory" };
            }
            
            directories.adultUnderwearMale = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Белье и одежда  для женщин";
                s.relate = directories.adult.getId();
                s.aspects = new String[] { "directory" };
            }
            
            directories.adultUnderwearFemale = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Обувь";
                s.relate = directories.adult.getId();
                s.aspects = new String[] { "directory" };
            }
            
            directories.adultFootwear = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Аксессуары";
                s.relate = directories.adult.getId();
                s.aspects = new String[] { "directory" };
            }
            
            directories.adultAccessories = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Носки и колготки";
                s.relate = directories.adult.getId();
                s.aspects = new String[] { "directory" };
            }
            
            directories.adultSocks = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Головные уборы";
                s.relate = directories.adult.getId();
                s.aspects = new String[] { "directory" };
            }
            
            directories.adultHats = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Детские товары";
                s.aspects = new String[] { "directory" };
            }
            
            directories.children = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        return directories;
    }
}
