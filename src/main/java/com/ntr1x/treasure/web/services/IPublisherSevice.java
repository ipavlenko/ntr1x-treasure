package com.ntr1x.treasure.web.services;

import org.springframework.context.ApplicationEvent;

public interface IPublisherSevice {
    
    void publishEvent(ApplicationEvent event);
}
