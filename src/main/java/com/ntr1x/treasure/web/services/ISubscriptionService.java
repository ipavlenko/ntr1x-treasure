package com.ntr1x.treasure.web.services;

import javax.websocket.Session;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface ISubscriptionService {

    void subscribe(Session session);
    void unsubscribe(Session session);
    void handle(Session session, SubscriptionMessage message);
    
    void broadcast(ResourceMessage message);
    
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubscriptionMessage {
        
        public static enum Type {
            SUBSCRIBE,
            UNSUBSCRIBE
        }
        
//        @XmlAttribute
        public Type type;
        
//        @XmlAttribute
        public String pattern;
    }
    
    @XmlRootElement
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ResourceMessage {
        
        public String alias;
        public Type type;
        
        @XmlElement
        public Object object;
        
        public static enum Type {
            CREATE,
            REMOVE,
            UPDATE
        }
    }
}
