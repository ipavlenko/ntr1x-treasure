package com.ntr1x.treasure.web.endpoints;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.services.ISerializationService;
import com.ntr1x.treasure.web.services.ISubscriptionService;
import com.ntr1x.treasure.web.services.ISubscriptionService.SubscriptionMessage;

@Component
@Scope("singleton")
@ServerEndpoint(value = "/socket/resources", configurator = EndpointConfigurator.class)
public class ResourceEndpoint {
    
    @Inject
    private ISubscriptionService subscriptions;
    
    @Inject
    private ISerializationService serialization;
    
//    @Inject
//    private ObjectProvider<ObjectGraph> provider;
    
    @PostConstruct
    private void init() {
//        System.out.println(provider);
    }
    
    @OnOpen
    public void onConnect(Session session) {
        
        subscriptions.subscribe(session);
    }
    
    @OnClose
    public void onClose(Session session) {
        
        subscriptions.unsubscribe(session);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        
        subscriptions.handle(session, serialization.parse(SubscriptionMessage.class, message, SubscriptionMessage.class));
    }
    
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("APP-Socket: Socket connection error");
        error.printStackTrace();
    }
}
