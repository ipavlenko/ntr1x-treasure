package com.ntr1x.treasure.web.services;

import java.time.LocalDateTime;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.ntr1x.treasure.web.converter.AppConverterProvider.LocalDateTimeConverter;
import com.ntr1x.treasure.web.model.p1.Tag;
import com.ntr1x.treasure.web.model.p2.ResourceCategory;
import com.ntr1x.treasure.web.model.p2.ResourceImage;
import com.ntr1x.treasure.web.model.p3.Publication;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IPublicationService {

    Publication create(PublicationCreate request);
    Publication update(long id, PublicationUpdate request);
    Publication remove(long id);
    PublicationsResponse.PublicationItem select(long id);
    
    PublicationsResponse search(
        int page,
        int size,
        String query,
        LocalDateTime since,
        LocalDateTime until, 
        Long[][] array
    );
    
    PublicationsResponse list(int page, int size);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicationsResponse {
        
        public long count;
        public int page;
        public int size;
        public List<PublicationItem> publications;
        
        @XmlRootElement
        @NoArgsConstructor
        @AllArgsConstructor
        public static class PublicationItem {
            
            public Publication publication;
            public List<Tag> tags;
            public List<ResourceImage> images;
            public List<ResourceCategory> categories;
        }
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicationCreate {
        
        public String title;
        public String promo;
        public String content;
        
        @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
        public LocalDateTime published;
        
        @XmlElement
        public ITagService.RelatedTag[] tags;
        
        @XmlElement
        public IImageService.RelatedImage[] images;
        
        @XmlElement
        public ICategoryService.RelatedCategory[] categories;
    }

    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PublicationUpdate {
        
        public String title;
        public String promo;
        public String content;
        
        @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
        public LocalDateTime published;
        
        @XmlElement
        public ITagService.RelatedTag[] tags;
        
        @XmlElement
        public IImageService.RelatedImage[] images;
        
        @XmlElement
        public ICategoryService.RelatedCategory[] categories;
    }
}
