package com.ntr1x.treasure.web.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Provider;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IProviderService {

    Provider create(ProviderCreate create);
    Provider update(long id, ProviderUpdate update);
    Provider remove(long id);
    
    ProvidersResponse list(int page, int size);
    Provider select(long id);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProviderCreate {
        
        public String title;
        public String promo;
        public String description;
        public long user;
        
        @XmlElement
        public IAttributeService.CreateAttribute[] attributes;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProviderUpdate {
        
        public String title;
        public String promo;
        public String description;
        public long user;
        
        @XmlElement
        public IAttributeService.UpdateAttribute[] attributes;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvidersResponse {
        
        public long count;
        public int page;
        public int size;
        public List<Provider> providers;
    }
}
