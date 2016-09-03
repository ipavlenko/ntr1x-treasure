package com.ntr1x.treasure.web.bootstrap;

import javax.xml.bind.annotation.XmlRootElement;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

public interface IBootstrap {
    
    public BootstrapResults bootstrap();
    
    @XmlRootElement
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BootstrapResults {
        
        public String userToken;
        public String adminToken;
    }
}
