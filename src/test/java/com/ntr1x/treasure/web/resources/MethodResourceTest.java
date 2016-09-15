package com.ntr1x.treasure.web.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

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
import com.ntr1x.treasure.web.model.p2.Method;
import com.ntr1x.treasure.web.services.IGrantService;
import com.ntr1x.treasure.web.services.IMethodService;
import com.ntr1x.treasure.web.services.IMethodService.MethodsResponse;
import com.ntr1x.treasure.web.services.IProfilerService;
import com.ntr1x.treasure.web.services.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-test.properties" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MethodResourceTest {

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
    public void test() {
        
        
        User[] users = { null, null };
        
        profiler.withDisabledSecurity(() -> {
            
            {
                IUserService.CreateUser s = new IUserService.CreateUser(); {
                    
                    s.role = User.Role.SELLER;
                    s.confirmed = true;
                    s.email = "user0@example.com";
                    s.password = "user0";
                    s.phone = "user0phone";
                    s.surname = "test";
                    s.name = "test";
                    s.middlename = "test";
                    s.confirmed = true;
                }
                
                User user = target
                    .path("/users")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), User.class)
                ;
                
                Assert.assertNotNull(user.getId());
                
                users[0] = user;
            }
            
            {
                IUserService.CreateUser s = new IUserService.CreateUser(); {
                    
                    s.role = User.Role.SELLER;
                    s.confirmed = true;
                    s.email = "user1@example.com";
                    s.password = "user1";
                    s.phone = "user1phone";
                    s.surname = "test";
                    s.name = "test";
                    s.middlename = "test";
                    s.confirmed = true;
                    s.grants = new IGrantService.CreateGrant[] {
                        new IGrantService.CreateGrant("/methods", "admin")
                    };
                }
                
                User user = target
                    .path("/users")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), User.class)
                ;
                
                Assert.assertNotNull(user.getId());
                
                users[1] = user;
            }
        });
        
        Method[] methods = { null, null };
        
        profiler.withCredentials(target, "user0@example.com", "user0", (token) -> {
           
            User user = target
                .path("/me")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(User.class)
            ;
            
            {
                IMethodService.MethodCreate s = new IMethodService.MethodCreate(); {
                    s.title = "Demo Method";
                    s.user = user.getId();
                }
                
                Method r = target
                    .path("/me/methods")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Method.class)
                ;
                
                Assert.assertNotNull(r.getId());
                Assert.assertEquals(s.title, r.getTitle());
                Assert.assertNull(r.getPurchases());
                
                methods[0] = r;
            }
            
            {
                List<Method> r = target
                    .path("/me/methods")
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(new GenericType<List<Method>>() {})
                ;
                
                Assert.assertEquals(1, r.size());
            }
            
            {
                MethodsResponse r = target
                    .path("/methods")
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(MethodsResponse.class)
                ;
                

                Assert.assertNotNull(r.methods);
                Assert.assertEquals(1, r.count);
                Assert.assertEquals(1, r.methods.size());
            }
        });
        
        profiler.withCredentials(target, "user1@example.com", "user1", (token) -> {
            
            User user = target
                .path("/me")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(User.class)
            ;
            
            {
                IMethodService.MethodCreate s = new IMethodService.MethodCreate(); {
                    s.title = "Demo Method";
                    s.user = user.getId();
                }
                
                Method r = target
                    .path("/methods")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Method.class)
                ;
                
                Assert.assertNotNull(r.getId());
                Assert.assertEquals(s.title, r.getTitle());
                Assert.assertNull(r.getPurchases());
                
                methods[1] = r;
            }

            {
                List<Method> r = target
                    .path("/me/methods")
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(new GenericType<List<Method>>() {})
                ;
                
                Assert.assertEquals(1, r.size());
            }
            
            {
                MethodsResponse r = target
                    .path("/methods")
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(MethodsResponse.class)
                ;
                

                Assert.assertNotNull(r.methods);
                Assert.assertEquals(2, r.count);
                Assert.assertEquals(2, r.methods.size());
            }
            
        });
        
        profiler.withDisabledSecurity(() -> {
            
            {
                Method r = target
                    .path(String.format("/methods/i/%d", methods[0].getId()))
                    .request()
                    .delete(Method.class)
                ;
                
                Assert.assertEquals(methods[0].getId(), r.getId());
            }
            
            {
                Method r = target
                    .path(String.format("/methods/i/%d", methods[1].getId()))
                    .request()
                    .delete(Method.class)
                ;
                
                Assert.assertEquals(methods[1].getId(), r.getId());
            }
            
            {
                User r = target
                    .path(String.format("/users/i/%d", users[0].getId()))
                    .request()
                    .delete(User.class)
                ;
                
                Assert.assertEquals(users[0].getId(), r.getId());
            }
            
            {
                User r = target
                    .path(String.format("/users/i/%d", users[1].getId()))
                    .request()
                    .delete(User.class)
                ;
                
                Assert.assertEquals(users[1].getId(), r.getId());
            }
        });
    }
}
