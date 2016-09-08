package com.ntr1x.treasure.web.services;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.Resource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IParamService {

    void createParams(Resource resource, CreateParam[] params);

    void updateParams(Resource resource, UpdateParam[] params);
    
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
        public Action action;
    }
}
