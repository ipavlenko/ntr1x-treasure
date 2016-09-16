package com.ntr1x.treasure.web.services;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.p3.Good;
import com.ntr1x.treasure.web.model.p4.Modification;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IModificationService {
    
    Modification create(ModificationCreate create);
    Modification update(long id, ModificationUpdate update);
    Modification remove(long id);
    
    Modification select(long id);
    
    ModificationsResponse list(int page, int size);
    
    void createModifications(Good resource, RelatedModification[] modifications);
    void updateModifications(Good resource, RelatedModification[] modifications);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModificationCreate {
      
        public long good;
        public float price;
        public float quantity;
        public float sizeRange;
      
        @XmlElement
        public IAttributeService.RelatedAttribute[] attributes;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModificationUpdate {
        
        public float price;
        public float quantity;
        public float sizeRange;
      
        @XmlElement
        public IAttributeService.RelatedAttribute[] attributes;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RelatedModification {
        
        public Long id;
        public float price;
        public float sizeRange;
        public Action action;
        
        @XmlElement
        public IAttributeService.RelatedAttribute[] attributes;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ModificationsResponse {
          
        public long count;
        public int page;
        public int size;
        public List<Modification> modifications;
    }
}
