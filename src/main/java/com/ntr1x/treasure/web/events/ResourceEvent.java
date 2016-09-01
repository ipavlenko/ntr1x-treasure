package com.ntr1x.treasure.web.events;

import org.springframework.context.ApplicationEvent;

import com.ntr1x.treasure.web.services.ISubscriptionService.ResourceMessage;

public class ResourceEvent extends ApplicationEvent {

    private static final long serialVersionUID = -5489703831389912509L;
    
    public ResourceEvent(ResourceMessage source) {
        super(source);
    }
    
    @Override
    public ResourceMessage getSource() {
        return (ResourceMessage) super.getSource();
    }
}