package com.ntr1x.treasure.web.services;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.p0.Resource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IImageService {

    void createImages(Resource resource, RelatedImage[] images);
    void updateImages(Resource resource, RelatedImage[] images);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedImage {
        
        public Long id;
        public Long image;
        public String[] aspects;
        public Action action;
    }
}
