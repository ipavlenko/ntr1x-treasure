package com.ntr1x.treasure.web.socket;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.services.ISubscriptionService;

@Component
@Scope("singleton")
@ServerEndpoint(value = "/socket", configurator = EndpointConfigurator.class)
public class ResourceEndpoint {
    
    @Inject
    private ISubscriptionService subscriptions;
    
    public ResourceEndpoint() {
        System.out.println("Created");
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
        System.out.println("APP-Socket: Socket connection message");
    }
    
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("APP-Socket: Socket connection error");
    }
}
