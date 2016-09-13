package com.ntr1x.treasure.web.services;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Resource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface ICategoryService {

    void createCategories(Resource resource, CreateCategory[] params);

    void updateCategories(Resource resource, UpdateCategory[] params);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateCategory {
        
        public Long category;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateCategory {
        
        public Long id;
        public Long category;
    }
}
