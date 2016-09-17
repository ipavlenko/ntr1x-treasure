package com.ntr1x.treasure.web.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.p3.Good;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IGoodService {

    Good create(CreateRequest request);
    Good update(long id, UpdateRequest request);
    Good remove(long id);
    Good select(long id);
    
    GoodsResponse list(int page, int size, Long purchase);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRequest {
        
        public String title;
        public String promo;
        public long purchase;
        
        @XmlElement
        public IImageService.RelatedImage[] images;
        
        @XmlElement
        public IAttributeService.RelatedAttribute[] attributes;
        
        @XmlElement
        public ICategoryService.RelatedCategory[] categories;
        
        @XmlElement
        public IModificationService.RelatedModification[] modifications;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateRequest {
        
        public String title;
        public String promo;
        
        @XmlElement
        public IImageService.RelatedImage[] images;
        
        @XmlElement
        public IAttributeService.RelatedAttribute[] attributes;
        
        @XmlElement
        public ICategoryService.RelatedCategory[] categories;
        
        @XmlElement
        public IModificationService.RelatedModification[] modifications;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GoodsResponse {
        
        public long count;
        public int page;
        public int size;
        public List<Good> goods;
    }
}
