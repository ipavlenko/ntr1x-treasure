package com.ntr1x.treasure.web.services;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.p0.Resource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IImageService {

    void createImages(Resource resource, CreateImage[] images);
    void updateImages(Resource resource, UpdateImage[] images);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateImage {
        
        public Long image;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateImage {
        
        public Long id;
        public Long image;
        public Action action;
    }
}
