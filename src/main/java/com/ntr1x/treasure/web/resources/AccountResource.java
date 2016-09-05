//package com.ntr1x.treasure.web.resources;
//
//import java.util.List;
//
//import javax.annotation.security.PermitAll;
//import javax.annotation.security.RolesAllowed;
//import javax.inject.Inject;
//import javax.persistence.EntityManager;
//import javax.transaction.Transactional;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.DELETE;
//import javax.ws.rs.GET;
//import javax.ws.rs.POST;
//import javax.ws.rs.PUT;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
//import javax.ws.rs.Produces;
//import javax.ws.rs.QueryParam;
//import javax.ws.rs.WebApplicationException;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.support.TransactionSynchronizationAdapter;
//import org.springframework.transaction.support.TransactionSynchronizationManager;
//
//import com.ntr1x.treasure.web.model.Account;
//import com.ntr1x.treasure.web.model.Action;
//import com.ntr1x.treasure.web.model.Grant;
//import com.ntr1x.treasure.web.reflection.ResourceUtils;
//import com.ntr1x.treasure.web.repository.AccountRepository;
//import com.ntr1x.treasure.web.services.ISecurityService;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiParam;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//
//@Path("accounts")
//@Api("Accounts")
//@PermitAll
//@Component
//public class AccountResource {
//	
//	@Inject
//	private EntityManager em;
//	
//	@Inject
//	private AccountRepository accounts;
//	
//	@Inject
//	private ISecurityService security;
//	
//	@GET
//    @Produces(MediaType.APPLICATION_JSON)
//	@RolesAllowed({ "res:///accounts:admin" })
//	@Transactional
//    public List<Account> list(
//    		@QueryParam("page") @ApiParam(example = "0") int page,
//    		@QueryParam("size") @ApiParam(example = "10") int size
//    ) {
//		return accounts.findAll(new PageRequest(0, size)).getContent();
//    }
//	
//	@GET
//	@Path("/{id}")
//	@Produces(MediaType.APPLICATION_JSON)
//	@RolesAllowed({ "res:///accounts:admin" })
//	@Transactional
//    public Account select(@PathParam("id") long id) {
//		
//	    return accounts.findOne(id);
//    }
//	
//	@POST
//	@Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//	@RolesAllowed({ "res:///accounts:admin" })
//	@Transactional
//    public Account create(AccountCreate account) {
//	
//	    Account persisted = new Account(); {
//	        
//	        int random = security.randomInt();
//	        persisted.setEmail(account.email);
//            persisted.setRandom(random);
//            persisted.setPwdhash(security.hashPassword(random, account.password));
//            
//            em.persist(persisted);
//            em.flush();
//            
//            persisted.setAlias(ResourceUtils.alias(null, "accounts", persisted));
//            
//            em.merge(persisted);
//            em.flush();
//            
//            if (account.grants != null) {
//                
//                for (AccountCreate.Grant grant : account.grants) {
//                    
//                    Grant g = new Grant(); {
//                        g.setAccount(persisted);
//                        g.setAction(grant.action);
//                        g.setPattern(grant.pattern);
//                    }
//                    
//                    em.persist(g);
//                }
//            }
//            
//            em.flush();
//	    }
//	    
//	    TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
//            
//            @Override
//            public void afterCommit() {
//                System.out.println(String.format("Persisted: %d", persisted.getId()));
//            }
//        });
//		
//		return persisted;
//    }
//	
//	@PUT
//	@Path("/{id}")
//	@Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
//	@RolesAllowed({ "res:///accounts:admin" })
//	@Transactional
//    public Account update(@PathParam("id") long id, AccountUpdate account) {
//		
//	    Account persisted = em.find(Account.class, id); {
//            
//	        int random = security.randomInt();
//            persisted.setEmail(account.email);
//            persisted.setRandom(random);
//            persisted.setPwdhash(security.hashPassword(random, account.password));
//            
//            em.merge(persisted);
//            em.flush();
//            
//            if (account.grants != null) {
//                
//                for (AccountUpdate.Grant grant : account.grants) {
//                    
//                    switch (grant._action) {
//                        case CREATE: {
//                            Grant g = new Grant(); {
//                                g.setAccount(persisted);
//                                g.setAction(grant.action);
//                                g.setPattern(grant.pattern);
//                                em.persist(g);
//                            }
//                            break;
//                        }
//                        case UPDATE: {
//                            Grant g = em.find(Grant.class, grant.id); {
//                                if (g.getAccount().getId() != persisted.getId()) {
//                                    throw new WebApplicationException("Grant belongs to another Account", Response.Status.CONFLICT);
//                                }
//                                g.setAction(grant.action);
//                                g.setPattern(grant.pattern);
//                                em.persist(g);
//                            }
//                            break;
//                        }
//                        case REMOVE: {
//                            Grant g = em.find(Grant.class, grant.id); {
//                                if (g.getAccount().getId() != persisted.getId()) {
//                                    throw new WebApplicationException("Grant belongs to another Account", Response.Status.CONFLICT);
//                                }
//                                em.remove(g);
//                            }
//                            break;
//                        }
//                        case IGNORE:
//                        default:
//                            break;
//                    }
//                }
//                
//                em.flush();
//            }
//        }
//        
//        return persisted;
//    }
//	
//	@DELETE
//	@Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//	@RolesAllowed({ "res:///accounts:admin" })
//	@Transactional
//    public Account remove(@PathParam("id") long id) {
//		
//	    Account persisted = em.find(Account.class, id); {
//            
//            em.remove(persisted);
//            em.flush();
//        }
//        
//        return persisted;
//    }
//	
//	@XmlRootElement
//    public static class AccountCreate {
//        
//        public String email;
//        public String password;
//        
//        @XmlElement
//        public Grant[] grants;
//        
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Grant {
//            
//            public String pattern;
//            public String action;
//        }
//    }
//    
//    @XmlRootElement
//    public static class AccountUpdate {
//        
//        public String email;
//        public String password;
//        
//        @XmlElement
//        public Grant[] grants;
//        
//        @NoArgsConstructor
//        @AllArgsConstructor
//        public static class Grant {
//            
//            public Long id;
//            public String pattern;
//            public String action;
//            public Action _action;
//        }
//    }
//}
