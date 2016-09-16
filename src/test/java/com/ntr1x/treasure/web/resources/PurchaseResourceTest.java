package com.ntr1x.treasure.web.resources;

import java.time.LocalDate;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
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
import com.ntr1x.treasure.web.model.p2.Provider;
import com.ntr1x.treasure.web.model.p2.Purchase;
import com.ntr1x.treasure.web.services.IMethodService;
import com.ntr1x.treasure.web.services.IProfilerService;
import com.ntr1x.treasure.web.services.IProviderService;
import com.ntr1x.treasure.web.services.IPurchaseService;
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
        });
        
        Purchase[] purchases = { null };
        Provider[] providers = { null };
        Method[] methods = { null };
        
        profiler.withCredentials(target, "user0@example.com", "user0", (token) -> {
           
            User user = target
                .path("/me")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .header(HttpHeaders.AUTHORIZATION, token)
                .get(User.class)
            ;
            
            Method method = null; {
                
                IMethodService.MethodCreate s = new IMethodService.MethodCreate(); {
                    s.title = "Demo method";
                    s.user = user.getId();
                }
                
                method = target
                    .path("/me/methods")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Method.class)
                ;
                
                methods[0] = method;
            }
            
            Provider provider = null; {
                
                IProviderService.ProviderCreate s = new IProviderService.ProviderCreate(); {
                    s.title = "Demo provider";
                    s.promo = "Demo provider promo";
                    s.description = "Demo provider description";
                    s.user = user.getId();
                }
                
                provider = target
                    .path("/me/providers")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Provider.class)
                ;
                
                providers[0] = provider;
            }
            
            Purchase purchase = null; {
                
                IPurchaseService.PurchaseCreate s = new IPurchaseService.PurchaseCreate(); {
                    
                    s.user = user.getId();
                    s.method = method.getId();
                    s.provider = provider.getId();
                    
                    s.title = "Demo Purchase";
                    s.open = LocalDate.now();
                    s.stop = LocalDate.now().plusWeeks(1);
                    s.delivery = LocalDate.now().plusWeeks(2);
                    s.nextDelivery = LocalDate.now().plusWeeks(3);
                }
                
                purchase = target
                    .path("/me/purchases")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Purchase.class)
                ;
                
                Assert.assertNotNull(purchase.getId());
                Assert.assertEquals(s.title, purchase.getTitle());
                Assert.assertEquals(s.open, purchase.getOpen());
                
                purchases[0] = purchase;
            }
            
            {
                IPurchaseService.DetailsResponse r = target
                    .path("/me/purchases")
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(IPurchaseService.DetailsResponse.class)
                ;
                
                Assert.assertEquals(1, r.count);
                Assert.assertEquals(1, r.purchases.size());
            }
            
            {
                IPurchaseService.PurchasesResponse r = target
                    .path("/purchases")
                    .queryParam("page", 0)
                    .queryParam("size", 10)
                    .request()
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .get(IPurchaseService.PurchasesResponse.class)
                ;
                

                Assert.assertNotNull(r.purchases);
                Assert.assertEquals(1, r.count);
                Assert.assertEquals(1, r.purchases.size());
            }
        });
        
        profiler.withDisabledSecurity(() -> {
            
            {
                Purchase r = target
                    .path(String.format("/purchases/i/%d", purchases[0].getId()))
                    .request()
                    .delete(Purchase.class)
                ;
                
                Assert.assertEquals(purchases[0].getId(), r.getId());
            }
            
            {
                Method r = target
                    .path(String.format("/methods/i/%d", methods[0].getId()))
                    .request()
                    .delete(Method.class)
                ;
                
                Assert.assertEquals(methods[0].getId(), r.getId());
            }
            
            {
                Provider r = target
                    .path(String.format("/providers/i/%d", providers[0].getId()))
                    .request()
                    .delete(Provider.class)
                ;
                
                Assert.assertEquals(providers[0].getId(), r.getId());
            }
            
            {
                User r = target
                    .path(String.format("/users/i/%d", users[0].getId()))
                    .request()
                    .delete(User.class)
                ;
                
                Assert.assertEquals(users[0].getId(), r.getId());
            }
        });
    }
}
