package com.ntr1x.treasure.web.services;

import java.security.Principal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.core.UriInfo;

public interface ISecurityContextService {

    boolean isUserInRole(UriInfoState info, String role);
    Principal getUserPrincipal();
    
    /**
     * UriInfo is mutable, UriInfoState stores relevant UriInfo
     */
    public static class UriInfoState {
        
        public final Map<String, Object> params;
        
        public UriInfoState(UriInfo info) {
                
            Map<String, Object> p = new HashMap<String, Object>();
            for (Entry<String, List<String>> entry : info.getPathParameters().entrySet()) {
                p.put(entry.getKey(), entry.getValue().get(0));
            }
            
            params = Collections.unmodifiableMap(p);
        }
    }
}
