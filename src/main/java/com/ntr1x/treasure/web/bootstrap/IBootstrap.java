package com.ntr1x.treasure.web.bootstrap;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.ntr1x.treasure.web.bootstrap.BootstrapState.Sessions;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IBootstrap {
    
    public BootstrapResults bootstrap();
    
    @XmlRootElement
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BootstrapResults {
        
        @XmlElement
        public Sessions sessions;
    }
}
