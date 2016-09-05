//package com.ntr1x.treasure.web.resources;
//package com.ntr1x.treasure.web.resources;
//
//import javax.annotation.security.PermitAll;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.transaction.Transactional;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import org.springframework.stereotype.Component;
//
//import com.ntr1x.treasure.web.model.Account;
//import com.ntr1x.treasure.web.model.Session;
//import com.ntr1x.treasure.web.reflection.ResourceUtils;
//import com.ntr1x.treasure.web.repository.AccountRepository;
//import com.ntr1x.treasure.web.services.ISecurityService;
//import com.ntr1x.treasure.web.utils.ConversionUtils;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//@Path("security")
//@Api("Security")
//@PermitAll
//@Component
//public class SecurityResource {
//    
//    @Inject
//    private EntityManager em;
//    
//    @Inject
//    private AccountRepository accounts;
//    
//    @Inject
//    private ISecurityService security;
//    
//    @Inject
//    private Session session;
//    
//    @POST
//    @ApiResponses({
//        @ApiResponse(code = 200, message = "OK"),
//        @ApiResponse(code = 401, message = "Unauthorized"),
//    })
//    @Path("/signin")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public SigninResponse signin(SigninRequest signin) {
//    
//        Account account = accounts.findByEmail(signin.email); {
//            
//            if (account == null) {
//                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
//            }
//            
//            if (!account.getPwdhash().equals(security.hashPassword(account.getRandom(), signin.password))) {
//                throw new WebApplicationException(Response.Status.UNAUTHORIZED);
//            }
//        }
//        
//        Session session = new Session(); {
//            
//            session.setAccount(account);
//            session.setSignature(security.randomInt());
//            
//            em.persist(session);
//            em.flush();
//            
//            session.setAlias(ResourceUtils.alias(account, "sessions", session));
//            
//            em.merge(session);
//            em.flush();
//        }
//        
//        String token = ConversionUtils.BASE62.encode(
//            security.encrypt(
//                security.toByteArray(new ISecurityService.SecuritySession(
//                    session.getId(),
//                    session.getSignature()
//                )
//            ))
//        );
//        
//        return new SigninResponse(token);
//    }
//    
//    @AllArgsConstructor
//    @XmlRootElement
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public static class SignoutResponse {
//    }
//    
//    @POST
//    @Path("/signout")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public SignoutResponse signout() {
//    
//        Session s = em.find(Session.class, session.getId());
//        em.remove(s);
//        em.flush();
//        return new SignoutResponse();
//    }
//    
//    @POST
//    @Path("/signup")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public SignupResponse signup(SignupRequest signup) {
//    
//        Account persisted = new Account(); {
//            
//            int random = security.randomInt();
//            persisted.setEmail(signup.email);
//            persisted.setRandom(random);
//            persisted.setPwdhash(security.hashPassword(random, signup.password));
//            
//            em.persist(persisted);
//            em.flush();
//        }
//        
//        return new SignupResponse();
//    }
//    
//    @XmlRootElement
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class SigninRequest {
//        
//        public String email;
//        public String password;
//    }
//    
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @XmlRootElement
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public static class SigninResponse {
//        
//        public String token;
//    }
//    
//    @XmlRootElement
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class SignupRequest {
//        
//        public String email;
//        public String password;
//    }
//    
//    @AllArgsConstructor
//    @XmlRootElement
//    @XmlAccessorType(XmlAccessType.FIELD)
//    public static class SignupResponse {
//    }
//}
