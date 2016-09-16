package com.ntr1x.treasure.web.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p1.Attribute;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IAttributeService {

    Attribute create(AttributeCreate create);
    Attribute update(long id, AttributeUpdate update);
    Attribute remove(long id);
    Attribute select(long id);
    
    AttributesResponse list(int page, int size, List<String> aspects);
    
    void createAttributes(Resource resource, RelatedAttribute[] attributes);
    void updateAttributes(Resource resource, RelatedAttribute[] attributes);
    
    void createOptions(Attribute attribute, RelatedOption[] options);
    void updateOptions(Attribute attribute, RelatedOption[] options);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeCreate {
        
        public String title;
        public boolean filter;
        public int order;
        public String[] aspects;
        
        @XmlElement
        public IAttributeService.RelatedOption[] options;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributeUpdate {
        
        public String title;
        public boolean filter;
        public int order;
        public String[] aspects;
        
        @XmlElement
        public IAttributeService.RelatedOption[] options;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AttributesResponse {
        
        public long count;
        public int page;
        public int size;
        public List<Attribute> attributes;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedAttribute {
        
        public Long id;
        public Long attribute;
        public String value;
        public Action action;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedOption {
        
        public Long id;
        public String title;
        public String value;
        public Action action;
    }
}
