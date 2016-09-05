package com.ntr1x.treasure.web.services;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.purchase.StoreAction;
import com.ntr1x.treasure.web.model.security.SecurityResource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IParamService {

    void createParams(SecurityResource resource, CreateParam[] params);

    void updateParams(SecurityResource resource, UpdateParam[] params);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateParam {
        
        public Long attribute;
        public String value;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateParam {
        
        public Long id;
        public Long attribute;
        public String value;
        public StoreAction action;
    }
}
