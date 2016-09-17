package com.ntr1x.treasure.web.services;

import com.ntr1x.treasure.web.events.ResourceEvent;

public interface IIndexService {
    
    void clear();

    void handle(ResourceEvent event);
}
