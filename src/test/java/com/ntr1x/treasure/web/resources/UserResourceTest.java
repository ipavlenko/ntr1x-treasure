package com.ntr1x.treasure.web.resources;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ntr1x.treasure.web.App;
import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.services.IGrantService;
import com.ntr1x.treasure.web.services.IProfilerService;
import com.ntr1x.treasure.web.services.IUserService;
import com.ntr1x.treasure.web.services.IUserService.UsersResponse;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-test.properties" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserResourceTest {

	@LocalServerPort
	private int port;
    
	@Inject
    private IProfilerService profiler;
	
	private WebTarget target;
	
	@Before
	public void init() {
	    
	    target = ClientBuilder
	        .newClient()
	        .target(String.format("http://localhost:%d", this.port))
	    ;
	}
	
	@Test
    public void test01Create() {
	    
	    profiler.withDisabledSecurity(() -> {
	        
	        IUserService.CreateUser s = new IUserService.CreateUser(); {
	            s.email = "user@example.com";
	            s.password = "123";
	            s.confirmed = true;
	        }
	        
	        User r = target
	            .path("/users")
	            .request(MediaType.APPLICATION_JSON_TYPE)
	            //.header("Authorization", context.getRootToken())
	            .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), User.class)
	        ;
	        
	        Assert.assertNotNull(r.getId());
	        Assert.assertEquals(s.email, r.getEmail());
	        Assert.assertNull(r.getPwdhash());
	    });
	}
	
	@Test
    public void test02List() {
	    
	    profiler.withDisabledSecurity(() -> {
	    
    	    UsersResponse response = target
                .path("/users")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .request()
                .get(UsersResponse.class)
            ;
            
            Assert.assertEquals(response.users.size(), 1);
	    });
	}
	
	@Test
    public void test03Select() {
	    
	    profiler.withDisabledSecurity(() -> {
	    
            User r = target
                .path(String.format("/users/i/%d", 1))
                .request()
                .get(User.class)
            ;
            
            Assert.assertTrue(r.getId() == 1);
            Assert.assertNull(r.getPwdhash());
	    });
	}
	
	@Test
    public void test04Modify() {
	    
	    profiler.withDisabledSecurity(() -> {
	    
	        IUserService.UpdateUser s = new IUserService.UpdateUser(); {
    	        s.email = "user@example.com";
    	        s.password = "1234";
    	        s.confirmed = true;
    	    }
    	    
            User r = target
                .path(String.format("/users/i/%d", 1))
                .request()
                .put(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), User.class)
            ;
            
            Assert.assertTrue(r.getId() == 1);
            Assert.assertNull(r.getPwdhash());
	    });
    }
	
	@Test
    public void test05Delete() {
	    
	    profiler.withDisabledSecurity(() -> {
	    
            User r = target
                .path(String.format("/users/i/%d", 1))
                .request()
                .delete(User.class)
            ;
            
            Assert.assertTrue(r.getId() == 1);
            Assert.assertNull(r.getPwdhash());
	    });
	}
	
	@Test
    public void test06PrepareCredentials() {
        
	    profiler.withDisabledSecurity(() -> {
	    
	        {
	            IUserService.CreateUser s = new IUserService.CreateUser(); {
                    s.email = "admin@example.com";
                    s.password = "9876";
                    s.confirmed = true;
                    s.grants = new IGrantService.CreateGrant[] {
                        new IGrantService.CreateGrant("/", "admin"),
                    };
                }
                
                User r = target
                    .path("/users")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    //.header("Authorization", context.getRootToken())
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), User.class)
                ;
                
                Assert.assertNotNull(r.getId());
                Assert.assertEquals(s.email, r.getEmail());
                Assert.assertNull(r.getPwdhash());
	        }
	        
	        {
	            IUserService.CreateUser s = new IUserService.CreateUser(); {
                    s.email = "user@example.com";
                    s.password = "1234";
                    s.confirmed = true;
                }
                
                User r = target
                    .path("/users")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    //.header("Authorization", context.getRootToken())
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), User.class)
                ;
                
                Assert.assertNotNull(r.getId());
                Assert.assertEquals(s.email, r.getEmail());
                Assert.assertNull(r.getPwdhash());
	        }
	    });
    }
	
	@Test
	public void test07AdminList() {
        
        profiler.withCredentials(target, "admin@example.com", "9876", (token) -> {
            
            Response response = target
                .path("/users")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .request()
                .header(HttpHeaders.AUTHORIZATION, token)
                .get()
            ;
            
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        });
    }
	
	@Test
    public void test08UserList() {
        
	    profiler.withCredentials(target, "user@example.com", "1234", (token) -> {
 
	        Response response = target
                .path("/accounts")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .request()
                .header(HttpHeaders.AUTHORIZATION, token)
                .get()
            ;
            
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
	    });
    }
	
	@Test
    public void test09NoodyList() {
        
        profiler.withCredentials(target, "nobody@example.com", "0000", (token) -> {
 
            Response response = target
                .path("/accounts")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .request()
                .header(HttpHeaders.AUTHORIZATION, token)
                .get()
            ;
            
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        });
    }
	
	@Test
    public void test10AdminCreate() {
        
        profiler.withCredentials(target, "admin@example.com", "9876", (token) -> {
            
            IUserService.CreateUser s = new IUserService.CreateUser(); {
                s.email = "another-user@example.com";
                s.password = "4567";
                s.confirmed = true;
            }
            
            Response response = target
                .path("/users")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE))
            ;
            
            Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        });
    }
	
	@Test
    public void test11UserCreate() {
        
        profiler.withCredentials(target, "user@example.com", "1234", (token) -> {
            
            IUserService.CreateUser s = new IUserService.CreateUser(); {
                s.email = "another-user@example.com";
                s.password = "4567";
                s.confirmed = true;
            }
            
            Response response = target
                .path("/users")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE))
            ;
            
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        });
    }
	
	@Test
    public void test12NobodyCreate() {
        
        profiler.withCredentials(target, "nobody@example.com", "0000", (token) -> {
            
            IUserService.CreateUser s = new IUserService.CreateUser(); {
                s.email = "another-user@example.com";
                s.password = "4567";
                s.confirmed = true;
            }
            
            Response response = target
                .path("/users")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header("Authorization", token)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE))
            ;
            
            Assert.assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        });
    }
}
