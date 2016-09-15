package com.ntr1x.treasure.web.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Method;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IMethodService {

    Method create(MethodCreate create);
    Method update(long id, MethodUpdate update);
    Method remove(long id);
    
    MethodsResponse list(int page, int size);
    Method select(long id);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MethodCreate {
        
        public String title;
        public long user;
        
        @XmlElement
        public IAttributeService.CreateAttribute[] attributes;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MethodUpdate {
        
        public String title;
        public long user;
        
        @XmlElement
        public IAttributeService.UpdateAttribute[] attributes;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MethodsResponse {
        
        public long count;
        public int page;
        public int size;
        public List<Method> methods;
    }
}
