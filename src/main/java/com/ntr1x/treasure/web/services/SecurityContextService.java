package com.ntr1x.treasure.web.services;

import java.security.Principal;
import java.text.MessageFormat;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.filters.AuthenticationFilter.AccountPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@Scope(value = "request", proxyMode = ScopedProxyMode.INTERFACES)
@RequiredArgsConstructor
public class SecurityContextService implements ISecurityContextService {

    @Inject
    private ISecurityService security;
    
    @Inject
    private HttpServletRequest request;

    @Inject
    private IProfilerService profiler;
    
    @Override
    public Principal getUserPrincipal() {
        return (AccountPrincipal) request.getAttribute(AccountPrincipal.class.getName());
    }
    
    @Override
    @Transactional
    public boolean isUserInRole(UriInfoState state, String role) {
        
        if (profiler.isSecurityDisabled()) return true;
        
        AccountPrincipal principal = (AccountPrincipal) request.getAttribute(AccountPrincipal.class.getName());
        
        if (principal == null
            || principal.session == null
            || principal.session.getAccount() == null
        ) return false;
            
        switch (role) {
            case "auth": return true;
        }
        
        if (role.startsWith("res://")) {
            
            String name = MessageFormat.format(role, state.params).substring("res://".length());
            
            int pos = name.indexOf(':');
            if (pos >= 0) {
                
                String resource = name.substring(0, pos);
                String action = name.substring(pos + 1);

                return security.isUserInRole(principal.getSession().getAccount(), resource, action);
            }
            
            return false;
        }
        
        return false;
    }
}
