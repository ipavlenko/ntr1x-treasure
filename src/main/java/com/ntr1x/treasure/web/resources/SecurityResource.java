package com.ntr1x.treasure.web.resources;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.model.Account;
import com.ntr1x.treasure.web.model.Session;
import com.ntr1x.treasure.web.repository.AccountRepository;
import com.ntr1x.treasure.web.repository.SessionRepository;
import com.ntr1x.treasure.web.services.ISecurityService;
import com.ntr1x.treasure.web.services.ResourceService;
import com.ntr1x.treasure.web.utils.ConversionUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Api("Security")
@Path("security")
@Component
public class SecurityResource {
    
    @Inject
    private ResourceService service;
    
    @Inject
    private AccountRepository accounts;
    
    @Inject
    private SessionRepository sessions;
    
    @Inject
    private ISecurityService security;
    
    @Inject
//    @RequestAttribute
    private Session session;
    
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SigninRequest {
        
        public String email;
        public String password;
    }
    
    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SigninResponse {
        
        public String token;
    }
    
    @POST
    @ApiResponses({
        @ApiResponse(code = 200, message = "OK"),
        @ApiResponse(code = 401, message = "Unauthorized"),
    })
    @Path("signin")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public SigninResponse signin(SigninRequest signin) {
    
        Account account = accounts.findByEmail(signin.email);
        
        if (!account.getPwdhash().equals(security.hashPassword(account.getRandom(), signin.password))) {
            throw new WebApplicationException(Response.Status.UNAUTHORIZED);
        }
        
        Session session = new Session();
        session.setAccount(account);
        session.setSignature(security.randomInt());
        Session s = (Session) service.create(account, session, "sessions");
        
        
        String token = ConversionUtils.BASE62.encode(
            security.encrypt(
                security.toByteArray(new ISecurityService.SecuritySession(
                    s.getId(),
                    s.getSignature()
                )
            ))
        );
        
        return new SigninResponse(token);
    }
    
    @AllArgsConstructor
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SignoutResponse {
    }
    
    @POST
    @Path("signout")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public SignoutResponse signout(SigninRequest signin) {
    
        sessions.delete(session);
        return new SignoutResponse();
    }
    
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SignupRequest {
        
        public String email;
        public String password;
    }
    
    @AllArgsConstructor
    @XmlRootElement
    @XmlAccessorType(XmlAccessType.FIELD)
    public static class SignupResponse {
    }
    
    @POST
    @Path("signup")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public SignupResponse signup(SignupRequest signup) {
    
        int random = security.randomInt();
        
        Account account = new Account();
        account.setEmail(signup.email);
        account.setRandom(random);
        account.setPwdhash(security.hashPassword(random, signup.password));
        
        service.create(null, account, "accounts");
        
        return new SignupResponse();
    }
}
