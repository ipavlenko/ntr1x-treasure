package com.ntr1x.treasure.web.events;

import org.springframework.context.ApplicationEvent;

import com.ntr1x.treasure.web.model.Resource;

public class ResourceEvent extends ApplicationEvent {

    private static final long serialVersionUID = -5489703831389912509L;
    
    protected ResourceEvent(Resource source) {
        super(source);
    }
    
    @Override
    public Resource getSource() {
        return (Resource) super.getSource();
    }
    
    public static class CREATED extends ResourceEvent {

        private static final long serialVersionUID = -7964676474978411466L;
        
        public CREATED(Resource source) {
            super(source);
        }
    }
    
    public static class UPDATED extends ResourceEvent {
        
        private static final long serialVersionUID = 8052130480118921832L;

        public UPDATED(Resource source) {
            super(source);
        }
    }
    
    public static class REMOVED extends ResourceEvent {
        
        private static final long serialVersionUID = 246167139354829430L;

        public REMOVED(Resource source) {
            super(source);
        }
    }
}