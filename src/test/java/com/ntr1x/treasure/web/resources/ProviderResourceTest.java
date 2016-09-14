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
import com.ntr1x.treasure.web.model.Provider;
import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.services.IGrantService;
import com.ntr1x.treasure.web.services.IProfilerService;
import com.ntr1x.treasure.web.services.IProviderService;
import com.ntr1x.treasure.web.services.IProviderService.ProvidersResponse;
import com.ntr1x.treasure.web.services.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-test.properties" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProviderResourceTest {

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
                        new IGrantService.CreateGrant("/providers", "admin")
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
        
        Provider[] providers = { null, null };
        
        profiler.withCredentials(target, "user0@example.com", "user0", (token) -> {
           
            User user = target
                .path("/me")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(User.class)
            ;
            
            {
                IProviderService.ProviderCreate s = new IProviderService.ProviderCreate(); {
                    s.title = "Demo Provider";
                    s.user = user.getId();
                    s.promo = "Demo Provider Promo";
                    s.description = "Demo Provider Decription";
                }
                
                Provider r = target
                    .path("/me/providers")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Provider.class)
                ;
                
                Assert.assertNotNull(r.getId());
                Assert.assertEquals(s.title, r.getTitle());
                Assert.assertEquals(s.promo, r.getPromo());
                Assert.assertEquals(s.description, r.getDescription());
                Assert.assertNull(r.getPurchases());
                
                providers[0] = r;
            }
            
            {
                List<Provider> r = target
                    .path("/me/providers")
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(new GenericType<List<Provider>>() {})
                ;
                
                Assert.assertEquals(1, r.size());
            }
            
            {
                ProvidersResponse r = target
                    .path("/providers")
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(ProvidersResponse.class)
                ;
                

                Assert.assertNotNull(r.providers);
                Assert.assertEquals(1, r.count);
                Assert.assertEquals(1, r.providers.size());
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
                IProviderService.ProviderCreate s = new IProviderService.ProviderCreate(); {
                    s.title = "Demo Provider";
                    s.user = user.getId();
                    s.promo = "Demo Provider Promo";
                    s.description = "Demo Provider Decription";
                }
                
                Provider r = target
                    .path("/providers")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Provider.class)
                ;
                
                Assert.assertNotNull(r.getId());
                Assert.assertEquals(s.title, r.getTitle());
                Assert.assertEquals(s.promo, r.getPromo());
                Assert.assertEquals(s.description, r.getDescription());
                Assert.assertNull(r.getPurchases());
                
                providers[1] = r;
            }
            

            {
                List<Provider> r = target
                    .path("/me/providers")
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(new GenericType<List<Provider>>() {})
                ;
                
                Assert.assertEquals(1, r.size());
            }
            
            {
                ProvidersResponse r = target
                    .path("/providers")
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(ProvidersResponse.class)
                ;
                

                Assert.assertNotNull(r.providers);
                Assert.assertEquals(2, r.count);
                Assert.assertEquals(2, r.providers.size());
            }
            
        });
        
        profiler.withDisabledSecurity(() -> {
            
            {
                Provider r = target
                    .path(String.format("/providers/i/%d", providers[0].getId()))
                    .request()
                    .get(Provider.class)
                ;
                
                Assert.assertEquals(providers[0].getId(), r.getId());
            }
            
            {
                Provider r = target
                    .path(String.format("/providers/i/%d", providers[1].getId()))
                    .request()
                    .get(Provider.class)
                ;
                
                Assert.assertEquals(providers[1].getId(), r.getId());
            }
            
            {
                User r = target
                    .path(String.format("/users/i/%d", users[0].getId()))
                    .request()
                    .get(User.class)
                ;
                
                Assert.assertEquals(users[0].getId(), r.getId());
            }
            
            {
                User r = target
                    .path(String.format("/users/i/%d", users[1].getId()))
                    .request()
                    .get(User.class)
                ;
                
                Assert.assertEquals(users[1].getId(), r.getId());
            }
        });
    }
}
