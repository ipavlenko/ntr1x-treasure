package com.ntr1x.treasure.web.resources;

import java.time.LocalDate;
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
import com.ntr1x.treasure.web.model.Purchase;
import com.ntr1x.treasure.web.model.User;
import com.ntr1x.treasure.web.services.IGrantService;
import com.ntr1x.treasure.web.services.IProfilerService;
import com.ntr1x.treasure.web.services.IPurchaseService;
import com.ntr1x.treasure.web.services.IPurchaseService.PurchasesResponse;
import com.ntr1x.treasure.web.services.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-test.properties" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PurchaseResourceTest {

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
                        new IGrantService.CreateGrant("/purchases", "admin")
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
        
        Purchase[] purchases = { null, null };
        
        profiler.withCredentials(target, "user0@example.com", "user0", (token) -> {
           
            User user = target
                .path("/me")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(User.class)
            ;
            
            {
                IPurchaseService.PurchaseCreate s = new IPurchaseService.PurchaseCreate(); {
                    s.user = user.getId();
                    s.title = "Demo Purchase";
                    s.open = LocalDate.now();
                    s.stop = LocalDate.now().plusWeeks(1);
                    s.delivery = LocalDate.now().plusWeeks(2);
                    s.nextDelivery = LocalDate.now().plusWeeks(3);
                    
                }
                
                Purchase r = target
                    .path("/me/purchases")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Purchase.class)
                ;
                
                Assert.assertNotNull(r.getId());
                Assert.assertEquals(s.title, r.getTitle());
                Assert.assertEquals(s.promo, r.getPromo());
                Assert.assertEquals(s.description, r.getDescription());
                Assert.assertNull(r.getPurchases());
                
                purchases[0] = r;
            }
            
            {
                List<Purchase> r = target
                    .path("/me/purchases")
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(new GenericType<List<Purchase>>() {})
                ;
                
                Assert.assertEquals(1, r.size());
            }
            
            {
                PurchasesResponse r = target
                    .path("/purchases")
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(PurchasesResponse.class)
                ;
                

                Assert.assertNotNull(r.purchases);
                Assert.assertEquals(1, r.count);
                Assert.assertEquals(1, r.purchases.size());
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
                IPurchaseService.PurchaseCreate s = new IPurchaseService.PurchaseCreate(); {
                    s.title = "Demo Purchase";
                    s.user = user.getId();
                    s.promo = "Demo Purchase Promo";
                    s.description = "Demo Purchase Decription";
                }
                
                Purchase r = target
                    .path("/purchases")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Purchase.class)
                ;
                
                Assert.assertNotNull(r.getId());
                Assert.assertEquals(s.title, r.getTitle());
                Assert.assertEquals(s.promo, r.getPromo());
                Assert.assertEquals(s.description, r.getDescription());
                Assert.assertNull(r.getPurchases());
                
                purchases[1] = r;
            }
            

            {
                List<Purchase> r = target
                    .path("/me/purchases")
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(new GenericType<List<Purchase>>() {})
                ;
                
                Assert.assertEquals(1, r.size());
            }
            
            {
                PurchasesResponse r = target
                    .path("/purchases")
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(PurchasesResponse.class)
                ;
                

                Assert.assertNotNull(r.purchases);
                Assert.assertEquals(2, r.count);
                Assert.assertEquals(2, r.purchases.size());
            }
            
        });
        
        profiler.withDisabledSecurity(() -> {
            
            {
                Purchase r = target
                    .path(String.format("/purchases/i/%d", purchases[0].getId()))
                    .request()
                    .get(Purchase.class)
                ;
                
                Assert.assertEquals(purchases[0].getId(), r.getId());
            }
            
            {
                Purchase r = target
                    .path(String.format("/purchases/i/%d", purchases[1].getId()))
                    .request()
                    .get(Purchase.class)
                ;
                
                Assert.assertEquals(purchases[1].getId(), r.getId());
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
