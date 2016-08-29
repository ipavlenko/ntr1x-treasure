package com.ntr1x.treasure.web.services;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.websocket.Session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.events.ResourceEvent;

@Service
@Scope("singleton")
public class SubscriptionService implements ISubscriptionService {

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    
    private Set<Session> sessions = new HashSet<>();
    
    @EventListener
    public void handle(ResourceEvent event) {
        
        broadcast(
            String.format(
                "Event: %s/%d",
                event.getSource().getClass().getSimpleName(),
                event.getSource().getId()
            )
        );
    }
    
    @Override
    public void subscribe(Session session) {
        
        executor.submit(() -> {
            sessions.add(session);
        });
    }

    @Override
    public void unsubscribe(Session session) {
        
        executor.submit(() -> {
            sessions.remove(session);
        });
    }
    
    @Override
    public void broadcast(String message) {
        
        executor.submit(() -> {
            
            for (Session session : sessions) {
                
                try {
                    
                    session.getBasicRemote().sendText(
                        message
                    );
                    
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });
    }
}
