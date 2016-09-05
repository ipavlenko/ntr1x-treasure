//package com.ntr1x.treasure.web.resources;
//
//import java.util.List;
//
//import javax.inject.Inject;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.GenericType;
//import javax.ws.rs.core.HttpHeaders;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.boot.context.embedded.LocalServerPort;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.ntr1x.treasure.web.App;
//import com.ntr1x.treasure.web.model.Account;
//import com.ntr1x.treasure.web.resources.AccountResource.AccountCreate;
//import com.ntr1x.treasure.web.resources.AccountResource.AccountUpdate;
//import com.ntr1x.treasure.web.services.IProfilerService;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-test.properties" })
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class AccountResourceTest {
//
//	@LocalServerPort
//	private int port;
//    
//	@Inject
//    private IProfilerService profiler;
//	
//	private WebTarget target;
//	
//	@Before
//	public void init() {
//	    
//	    target = ClientBuilder
//	        .newClient()
//	        .target(String.format("http://localhost:%d", this.port))
//	    ;
//	}
//	
//	@Test
//    public void test01Create() {
//	    
//	    profiler.withDisabledSecurity(() -> {
//	        
//	        AccountCreate s = new AccountCreate(); {
//	            s.email = "user@example.com";
//	            s.password = "123";
//	        }
//	        
//	        Account r = target
//	            .path("/accounts")
//	            .request(MediaType.APPLICATION_JSON_TYPE)
//	            //.header("Authorization", context.getRootToken())
//	            .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Account.class)
//	        ;
//	        
//	        Assert.assertNotNull(r.getId());
//	        Assert.assertEquals(s.email, r.getEmail());
//	        Assert.assertNull(r.getPwdhash());
//	    });
//	}
//	
//	@Test
//    public void test02List() {
//	    
//	    profiler.withDisabledSecurity(() -> {
//	    
//    	    List<Account> response = target
//                .path("/accounts")
//                .queryParam("page", 0)
//                .queryParam("size", 10)
//                .request()
//                .get(new GenericType<List<Account>>() {})
//            ;
//            
//            Assert.assertEquals(response.size(), 1);
//	    });
//	}
//	
//	@Test
//    public void test03Select() {
//	    
//	    profiler.withDisabledSecurity(() -> {
//	    
//            Account r = target
//                .path(String.format("/accounts/%d", 1))
//                .request()
//                .get(Account.class)
//            ;
//            
//            Assert.assertTrue(r.getId() == 1);
//            Assert.assertNull(r.getPwdhash());
//	    });
//	}
//	
//	@Test
//    public void test04Modify() {
//	    
//	    profiler.withDisabledSecurity(() -> {
//	    
//    	    AccountUpdate s = new AccountUpdate(); {
//    	        s.email = "user@example.com";
//    	        s.password = "1234";
//    	    }
//    	    
//            Account r = target
//                .path(String.format("/accounts/%d", 1))
//                .request()
//                .put(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Account.class)
//            ;
//            
//            Assert.assertTrue(r.getId() == 1);
//            Assert.assertNull(r.getPwdhash());
//	    });
//    }
//	
//	@Test
//    public void test05Delete() {
//	    
//	    profiler.withDisabledSecurity(() -> {
//	    
//            Account r = target
//                .path(String.format("/accounts/%d", 1))
//                .request()
//                .delete(Account.class)
//            ;
//            
//            Assert.assertTrue(r.getId() == 1);
//            Assert.assertNull(r.getPwdhash());
//	    });
//	}
//	
//	@Test
//    public void test06PrepareCredentials() {
//        
//	    profiler.withDisabledSecurity(() -> {
//	    
//	        {
//                AccountCreate s = new AccountCreate(); {
//                    s.email = "admin@example.com";
//                    s.password = "9876";
//                    s.grants = new AccountCreate.Grant[] {
//                        new AccountCreate.Grant("/", "admin"),
//                    };
//                }
//                
//                Account r = target
//                    .path("/accounts")
//                    .request(MediaType.APPLICATION_JSON_TYPE)
//                    //.header("Authorization", context.getRootToken())
//                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Account.class)
//                ;
//                
//                Assert.assertNotNull(r.getId());
//                Assert.assertEquals(s.email, r.getEmail());
//                Assert.assertNull(r.getPwdhash());
//	        }
//	        
//	        {
//	            AccountCreate s = new AccountCreate(); {
//                    s.email = "user@example.com";
//                    s.password = "1234";
//                }
//                
//                Account r = target
//                    .path("/accounts")
//                    .request(MediaType.APPLICATION_JSON_TYPE)
//                    //.header("Authorization", context.getRootToken())
//                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Account.class)
//                ;
//                
//                Assert.assertNotNull(r.getId());
//                Assert.assertEquals(s.email, r.getEmail());
//                Assert.assertNull(r.getPwdhash());
//	        }
//	    });
//    }
//	
//	@Test
//	public void test07AdminList() {
//        
//        profiler.withCredentials(target, "admin@example.com", "9876", (token) -> {
//            
//            Response response = target
//                .path("/accounts")
//                .queryParam("page", 0)
//                .queryParam("size", 10)
//                .request()
//                .header(HttpHeaders.AUTHORIZATION, token)
//                .get()
//            ;
//            
//            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//        });
//    }
//	
//	@Test
//    public void test08UserList() {
//        
//	    profiler.withCredentials(target, "user@example.com", "1234", (token) -> {
// 
//	        Response response = target
//                .path("/accounts")
//                .queryParam("page", 0)
//                .queryParam("size", 10)
//                .request()
//                .header(HttpHeaders.AUTHORIZATION, token)
//                .get()
//            ;
//            
//            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
//	    });
//    }
//	
//	@Test
//    public void test09NoodyList() {
//        
//        profiler.withCredentials(target, "nobody@example.com", "0000", (token) -> {
// 
//            Response response = target
//                .path("/accounts")
//                .queryParam("page", 0)
//                .queryParam("size", 10)
//                .request()
//                .header(HttpHeaders.AUTHORIZATION, token)
//                .get()
//            ;
//            
//            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
//        });
//    }
//	
//	@Test
//    public void test10AdminCreate() {
//        
//        profiler.withCredentials(target, "admin@example.com", "9876", (token) -> {
//            
//            AccountCreate s = new AccountCreate(); {
//                s.email = "another-user@example.com";
//                s.password = "4567";
//            }
//            
//            Response response = target
//                .path("/accounts")
//                .request(MediaType.APPLICATION_JSON_TYPE)
//                .header("Authorization", token)
//                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE))
//            ;
//            
//            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
//        });
//    }
//	
//	@Test
//    public void test11UserCreate() {
//        
//        profiler.withCredentials(target, "user@example.com", "1234", (token) -> {
//            
//            AccountCreate s = new AccountCreate(); {
//                s.email = "another-user@example.com";
//                s.password = "4567";
//            }
//            
//            Response response = target
//                .path("/accounts")
//                .request(MediaType.APPLICATION_JSON_TYPE)
//                .header("Authorization", token)
//                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE))
//            ;
//            
//            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
//        });
//    }
//	
//	@Test
//    public void test12NobodyCreate() {
//        
//        profiler.withCredentials(target, "nobody@example.com", "0000", (token) -> {
//            
//            AccountCreate s = new AccountCreate(); {
//                s.email = "another-user@example.com";
//                s.password = "4567";
//            }
//            
//            Response response = target
//                .path("/accounts")
//                .request(MediaType.APPLICATION_JSON_TYPE)
//                .header("Authorization", token)
//                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE))
//            ;
//            
//            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
//        });
//    }
//}
