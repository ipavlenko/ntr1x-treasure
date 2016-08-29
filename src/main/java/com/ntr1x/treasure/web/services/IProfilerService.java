package com.ntr1x.treasure.web.services;

import java.util.function.Consumer;

import javax.ws.rs.client.WebTarget;

public interface IProfilerService {
    
    boolean isSecurityDisabled();
    
    void withDisabledSecurity(Runnable runnable);
    void withCredentials(WebTarget target, String email, String password, Consumer<String> consumer);
}
