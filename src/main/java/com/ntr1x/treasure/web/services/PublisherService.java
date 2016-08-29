package com.ntr1x.treasure.web.services;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

@Service
public class PublisherService implements ApplicationEventPublisherAware, IPublisherSevice {

    private ApplicationEventPublisher publisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
    
    @Override
    public void publishEvent(ApplicationEvent event) {
        publisher.publishEvent(event);
    }   
}
