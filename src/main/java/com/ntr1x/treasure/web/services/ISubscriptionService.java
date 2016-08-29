package com.ntr1x.treasure.web.services;

import javax.websocket.Session;

public interface ISubscriptionService {

    void subscribe(Session session);
    void unsubscribe(Session session);
    
    void broadcast(String format);
}
