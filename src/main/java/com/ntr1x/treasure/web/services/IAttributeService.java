package com.ntr1x.treasure.web.services;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.p0.Resource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IAttributeService {

    void createAttributes(Resource resource, CreateAttribute[] attributes);

    void updateAttributes(Resource resource, UpdateAttribute[] attributes);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAttribute {
        
        public Long attribute;
        public String value;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAttribute {
        
        public Long id;
        public Long attribute;
        public String value;
        public Action action;
    }
}
