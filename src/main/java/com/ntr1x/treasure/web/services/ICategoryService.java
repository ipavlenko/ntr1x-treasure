package com.ntr1x.treasure.web.services;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.p0.Resource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface ICategoryService {

    void createCategories(Resource resource, RelatedCategory[] categories);

    void updateCategories(Resource resource, RelatedCategory[] categories);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedCategory {
        
        public Long id;
        public Long category;
        public Action action;
    }
}
