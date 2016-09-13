package com.ntr1x.treasure.web.bootstrap;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
public class BootstrapHolder {
    
    private BootstrapState state = new BootstrapState();
    
    public BootstrapState get() {
        return state;
    }
}
