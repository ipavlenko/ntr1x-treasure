package com.ntr1x.treasure.web.services;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import javax.inject.Inject;
import javax.websocket.Session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.events.ResourceEvent;

import lombok.RequiredArgsConstructor;
import lombok.Synchronized;

@Service
@Scope("singleton")
public class SubscriptionService implements ISubscriptionService {

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    
    private Map<Session, Subscription> subscriptions = new HashMap<>();
    
    @Inject
    private ISerializationService serialization;
    
    @EventListener
    public void handle(ResourceEvent event) {
        
        broadcast(event.getSource());
    }
    
    @Override
    public void subscribe(Session session) {
        
        executor.submit(() -> {
            subscriptions.put(session, new Subscription(session));
        });
    }

    @Override
    public void unsubscribe(Session session) {
        
        executor.submit(() -> {
            subscriptions.remove(session);
        });
    }
    
    @Override
    public void broadcast(ResourceMessage message) {
        
        executor.submit(() -> {
            
            for (Subscription subscription : subscriptions.values()) {
                
                subscription.send(message);
            }
        });
    }

    @Override
    public void handle(Session session, SubscriptionMessage message) {
        
        executor.submit(() -> {
            
            Subscription subscription = subscriptions.get(session);
            if (subscription != null) {
                subscription.handle(message);
            }
        });
    }
    
    @RequiredArgsConstructor
    private class Subscription {
        
        private final Session session;
        private final Map<String, Pattern> patterns = new HashMap<>();
        
        @Synchronized
        public void handle(SubscriptionMessage message) {
            switch (message.type) {
            case SUBSCRIBE:
                patterns.put(message.pattern, Pattern.compile(message.pattern));
                break;
            case UNSUBSCRIBE:
                patterns.remove(message.pattern);
                break;
            }
        }
        
        @Synchronized
        public void send(ResourceMessage message) {
            
            for (Pattern p : patterns.values()) {
                
                if (p.matcher(message.alias).matches()) {
                    
                    try {
                        
                        session.getBasicRemote().sendText(serialization.stringify(message.object, message.object.getClass()));
                    
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                    break;
                }
            }
        }
    }
}
