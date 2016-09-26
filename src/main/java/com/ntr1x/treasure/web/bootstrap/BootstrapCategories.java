package com.ntr1x.treasure.web.bootstrap;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import com.ntr1x.treasure.web.model.p1.Category;
import com.ntr1x.treasure.web.resources.CategoryResource.CategoryCreate;

public class BootstrapCategories {
    
    private Bootstrap bootstrap;

    public BootstrapCategories(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }
    
    public Directories createDirectories(WebTarget target, String token) {
        
        Directories directories = new Directories();
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Выставка";
                s.aspects = new String[] { "directory" };
            }
            
            directories.expose = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Конференции";
                s.aspects = new String[] { "directory" };
            }
            
            directories.conference = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Исследования";
                s.aspects = new String[] { "directory" };
            }
            
            directories.research = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Лекарства";
                s.aspects = new String[] { "directory" };
            }
            
            directories.medicine = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Лекарства";
                s.aspects = new String[] { "directory" };
            }
            
            directories.medicine = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            
            CategoryCreate s = new CategoryCreate(); {
                s.title = "В мире";
                s.aspects = new String[] { "directory" };
            }
            
            directories.globe = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        return directories;
    }
    
    public Specialiations createSpecializatons(WebTarget target, String token) {
        
        Specialiations specializations = new Specialiations();
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Артроскопия";
                s.aspects = new String[] { "specialization" };
            }
            
            specializations.arthroscopy = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Эндопротезирование";
                s.aspects = new String[] { "specialization" };
            }
            
            specializations.endoprosthesis = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Травма";
                s.aspects = new String[] { "specialization" };
            }
            
            specializations.trauma = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Специализация";
                s.aspects = new String[] { "specialization" };
            }
            
            specializations.specialization = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        return specializations;
    }
    
    public Localizations createLocalizations(WebTarget target, String token) {
        
        Localizations localizations = new Localizations();
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Голеностоп";
                s.relate = bootstrap.specializations.specialization.getId();
                s.aspects = new String[] { "localization" };
            }
            
            localizations.ankle = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Коленный сустав";
                s.relate = bootstrap.specializations.specialization.getId();
                s.aspects = new String[] { "localization" };
            }
            
            localizations.knee = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Тазобедренный сустав";
                s.relate = bootstrap.specializations.specialization.getId();
                s.aspects = new String[] { "localization" };
            }
            
            localizations.hip = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Плечевой сустав";
                s.relate = bootstrap.specializations.specialization.getId();
                s.aspects = new String[] { "localization" };
            }
            
            localizations.shoulder = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        {
            CategoryCreate s = new CategoryCreate(); {
                s.title = "Межфаланговый сустав";
                s.relate = bootstrap.specializations.specialization.getId();
                s.aspects = new String[] { "localization" };
            }
            
            localizations.interphalangeal = target
                .path("/categories")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
            ;
        }
        
        return localizations;
    }

    class Directories {
        
        Category expose;
        Category conference;
        Category research;
        Category medicine;
        Category globe;
    }
    
    class Specialiations {
        
        Category arthroscopy;
        Category endoprosthesis;
        Category trauma;
        Category specialization;
    }
    
    class Localizations {
        
        Category ankle;
        Category knee;
        Category hip;
        Category shoulder;
        Category interphalangeal;
    }
}
