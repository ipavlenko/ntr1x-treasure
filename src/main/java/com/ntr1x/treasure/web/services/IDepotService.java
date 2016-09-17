package com.ntr1x.treasure.web.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.p2.Depot;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IDepotService {

    Depot create(DepotCreate create);
    Depot update(long id, DepotUpdate update);
    Depot remove(long id);
    Depot select(long id);
    
    DepotsResponse list(int page, int size);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepotCreate {
        
        public String title;
        public float deliveryPrice;
        public long user;
        
        @XmlElement
        public IAttributeService.RelatedAttribute[] attributes;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepotUpdate {
        
        public String title;
        public float deliveryPrice;
        
        @XmlElement
        public IAttributeService.RelatedAttribute[] attributes;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DepotsResponse {
        
        public long count;
        public int page;
        public int size;
        public List<Depot> depots;
    }
}
