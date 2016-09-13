package com.ntr1x.treasure.web.resources;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
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
import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.resources.UsersResource.CreateUser;
import com.ntr1x.treasure.web.resources.UsersResource.UsersResponse;
import com.ntr1x.treasure.web.services.IProfilerService;

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
            
            CreateUser s = new CreateUser(); {
                
                s.role = User.Role.ADMIN;
                s.confirmed = true;
                s.email = "admin@example.com";
                s.password = "admin";
                s.phone = "00000000000";
                s.surname = "admin";
                s.userName = "admin";
                s.middleName = "admin";
                s.confirmed = true;
            }
            
            User r = target
              .path("/ws/users")
              .request(MediaType.APPLICATION_JSON_TYPE)
              .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), User.class)
            ;
            
            Assert.assertNotNull(r.getId());
            Assert.assertEquals(s.email, r.getEmail());
            Assert.assertNull(r.getPwdhash());
            Assert.assertNull(r.getRandom());
        });
    }
    
    @Test
    public void test02Select() {
        
        profiler.withDisabledSecurity(() -> {
            
            {
                User r = target
                    .path(String.format("/ws/users/i/%d", 1))
                    .queryParam("page", 0)
                    .request()
                    .get(User.class)
                ;
                  
                Assert.assertEquals(Long.valueOf(1), r.getId());
                Assert.assertNull(r.getPwdhash());
                Assert.assertNull(r.getRandom());
            }
        });
    }
    
    @Test
    public void test03List() {
    
        profiler.withDisabledSecurity(() -> {
        
            {
                UsersResponse r = target
                    .path("/ws/users")
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .request()
                    .get(UsersResponse.class)
                ;
                  
                Assert.assertEquals(1, r.count);
                Assert.assertEquals(10, r.size);
                Assert.assertEquals(0, r.page, 0);
                Assert.assertEquals(1, r.users.size());
            }
            
            {
                UsersResponse r = target
                    .path("/ws/users")
                    .queryParam("role", User.Role.ADMIN)
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .request()
                    .get(UsersResponse.class)
                ;
                  
                Assert.assertEquals(1, r.count);
                Assert.assertEquals(10, r.size);
                Assert.assertEquals(0, r.page, 0);
                Assert.assertEquals(1, r.users.size());
            }
            
            {
                UsersResponse r = target
                    .path("/ws/users")
                    .queryParam("role", User.Role.DELIVERY)
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .request()
                    .get(UsersResponse.class)
                ;
                  
                Assert.assertEquals(0, r.count);
                Assert.assertEquals(10, r.size);
                Assert.assertEquals(0, r.page, 0);
                Assert.assertEquals(0, r.users.size());
            }
        });
    }
    
    @Test
    public void test04Remove() {
        
        profiler.withDisabledSecurity(() -> {
            
            {
                User r = target
                    .path(String.format("/ws/users/i/%d", 1))
                    .queryParam("page", 0)
                    .request()
                    .delete(User.class)
                ;
                  
                Assert.assertEquals(Long.valueOf(1), r.getId());
                Assert.assertNull(r.getPwdhash());
                Assert.assertNull(r.getRandom());
            }
        });
    }
}
