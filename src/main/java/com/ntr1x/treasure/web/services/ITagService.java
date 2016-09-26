package com.ntr1x.treasure.web.services;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.p0.Resource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface ITagService {

    void createTags(Resource resource, RelatedTag[] tags);

    void updateTags(Resource resource, RelatedTag[] tags);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedTag {
        
        public Long id;
        public String value;
        public Action action;
    }
}
