package com.ntr1x.treasure.web.services;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.Resource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface ICategoryService {

    void createCategories(Resource resource, CreateCategory[] categories);

    void updateCategories(Resource resource, UpdateCategory[] categories);
    
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
        public Action action;
    }
}
