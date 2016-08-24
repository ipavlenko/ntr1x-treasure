//package com.ntr1x.treasure.web.resources;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//import javax.transaction.Transactional;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.Produces;
//import javax.ws.rs.core.MediaType;
//
//import org.springframework.stereotype.Component;
//
//import com.ntr1x.treasure.web.model.Account;
//import com.ntr1x.treasure.web.model.Action;
//import com.ntr1x.treasure.web.model.Grant;
//import com.ntr1x.treasure.web.model.Session;
//import com.ntr1x.treasure.web.services.ISecurityService;
//import com.ntr1x.treasure.web.services.ResourceService;
//import com.ntr1x.treasure.web.utils.ConversionUtils;
//
//import io.swagger.annotations.Api;
//
//@Api(value = "Setup", hidden = true)
//@Path("setup")
//@Component
//public class SetupResource {
//    
//    @Inject
//    private ResourceService service;
//    
//    @Inject
//    private ISecurityService security;
//    
//    @POST
//    @Path("/accounts")
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//    @Transactional
//    public SetupResponse setupAccount() {
//    
//        Session userSession = null; {
//            
//            Account a = new Account();
//            a.setEmail("user@example.com");
//            a.setPassword("123");
//            a.setRandom(security.randomInt());
//            
//            List<Session> sessions = new ArrayList<>(); {
//                
//                Session s = new Session();
//                s.setAccount(a);
//                s.set_action(Action.CREATE);
//                
//                sessions.add(s);
//            }
//
//            a.setSessions(sessions);
//            
//            Account account = (Account) service.create(null, a, "accounts");
//            
//            userSession = account.getSessions().get(0);
//        }
//        
//        Session rootSession = null; {
//            
//            Account a = new Account();
//            a.setEmail("root@example.com");
//            a.setPassword("234");
//            a.setRandom(security.randomInt());
//            
//            List<Grant> grants = new ArrayList<>(); {
//                
//                Grant g = new Grant();
//                g.setAccount(a);
//                g.setPattern("/");
//                g.setAction("admin");
//                g.set_action(Action.CREATE);
//                
//                grants.add(g);
//            }
//
//            a.setGrants(grants);
//            
//            List<Session> sessions = new ArrayList<>(); {
//                
//                Session s = new Session();
//                s.setAccount(a);
//                s.set_action(Action.CREATE);
//                
//                sessions.add(s);
//            }
//
//            a.setSessions(sessions);
//            
//            Account account = (Account) service.create(null, a, "accounts");
//            
//            rootSession = account.getSessions().get(0);
//        }
//        
//        return new SetupResponse(
//            ConversionUtils.BASE62.encode(
//                security.encrypt(
//                    security.toByteArray(
//                        new ISecurityService.SecuritySession(
//                            userSession.getId(),
//                            userSession.getSignature()
//                        )
//                    )
//                )
//            ),
//            ConversionUtils.BASE62.encode(
//                security.encrypt(
//                    security.toByteArray(
//                        new ISecurityService.SecuritySession(
//                            rootSession.getId(),
//                            rootSession.getSignature()
//                        )
//                    )
//                )
//            )
//        );
//    }
//    
//}
