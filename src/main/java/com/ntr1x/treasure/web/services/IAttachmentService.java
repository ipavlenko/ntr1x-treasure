package com.ntr1x.treasure.web.services;

import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.Resource;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IAttachmentService {
    
    void createAttachments(Resource resource, CreateAttachment[] params);

    void updateAttachments(Resource resource, UpdateAttachment[] params);
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateAttachment {
        
        public Long upload;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateAttachment {
        
        public Long id;
        public Long upload;
        public Action action;
    }
}
