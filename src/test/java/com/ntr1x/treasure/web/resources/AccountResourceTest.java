package com.ntr1x.treasure.web.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ntr1x.treasure.web.App;
import com.ntr1x.treasure.web.model.Account;
import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.Grant;
import com.ntr1x.treasure.web.services.IProfilerService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
public class AccountResourceTest {

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
	    
	    profiler.setSecurityDisabled(true);
	}
	
	@After
    public void destroy() {
        
	    profiler.setSecurityDisabled(false);
    }
    
	@Test
    public void testList() {
		
		{
			List<Account> response = target
	        	.path("/accounts")
				.queryParam("page", 0)
	        	.queryParam("size", 10)
	        	.request()
	        	//.header(Header.Authorization.toString(), context.getRootToken())
	        	.get(new GenericType<List<Account>>() {})
	        ;
	        
	        Assert.assertEquals(response.size(), 0);        
		}
		
	}
	
	@Test
    public void testModify() {
	    
		Account user = null; {
	        
			Account s = new Account();
	        s.setEmail("email@example.com");
	        s.setPassword("123");
	        s.set_action(Action.CREATE);
	        
	        Account r = target
		    	.path("/accounts")
		    	.request(MediaType.APPLICATION_JSON_TYPE)
		    	//.header("Authorization", context.getRootToken())
		    	.post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Account.class)
		    ;
	        
	        Assert.assertNotNull(r.getId());
	        Assert.assertEquals(s.getEmail(), r.getEmail());
	        Assert.assertNull(r.get_action());
	        Assert.assertNull(r.getPassword());
	        Assert.assertNull(r.getPwdhash());
		
	        user = r;
		}
		
		{
			List<Account> response = target
	        	.path("/accounts")
				.queryParam("page", 0)
	        	.queryParam("size", 10)
	        	.request()
	        	.get(new GenericType<List<Account>>() {})
	        ;
	        
	        Assert.assertEquals(response.size(), 1);
		}
		
		{
            Account r = target
                .path(String.format("/accounts/%d", user.getId()))
                .request()
                .get(Account.class)
            ;
            
            Assert.assertEquals(r.getId(), user.getId());
            Assert.assertNull(r.get_action());
            Assert.assertNull(r.getPassword());
            Assert.assertNull(r.getPwdhash());
        }
        
        {
            Account r = target
                .path(String.format("/accounts/%d/full", user.getId()))
                .request()
                .get(Account.class)
            ;
            
            Assert.assertEquals(r.getId(), user.getId());
            Assert.assertNull(r.get_action());
            Assert.assertNull(r.getPassword());
            Assert.assertNull(r.getPwdhash());
        }
        
        {
            Account r = target
                .path(String.format("/accounts/%d", user.getId()))
                .request()
                .delete(Account.class)
            ;
            
            Assert.assertEquals(r.getId(), user.getId());
            Assert.assertNull(r.get_action());
            Assert.assertNull(r.getPassword());
            Assert.assertNull(r.getPwdhash());
        }
	}
	
	@Test
    public void testSecurity() {
		
	    Account admin = null; {
            
            Account s = new Account();
            s.setEmail("email@example.com");
            s.setPassword("Password");
            s.set_action(Action.CREATE);
            
            Account r = target
                .path("/accounts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Account.class)
            ;
            
            Assert.assertNotNull(r.getId());
            Assert.assertEquals(s.getEmail(), r.getEmail());
            Assert.assertNull(r.get_action());
            Assert.assertNull(r.getPassword());
            Assert.assertNull(r.getPwdhash());
        
            admin = r;
        }
	    
	    {
	        
	        Grant g = new Grant();
	        g.setAccount(admin);
	        g.setPattern("/publications");
	        g.setAction("admin");
	        
	        Grant r = target
	            .path("/grants")
	            .request(MediaType.APPLICATION_JSON_TYPE)
	            .post(Entity.entity(g, MediaType.APPLICATION_JSON_TYPE), Grant.class)
	        ;
	        
	        Assert.assertNotNull(r.getId());
	        Assert.assertEquals(g.getPattern(), r.getPattern());
            Assert.assertEquals(g.getAction(), r.getAction());
            Assert.assertEquals(g.getAccount().getId(), r.getAccount().getId());
	    }
    }
}
