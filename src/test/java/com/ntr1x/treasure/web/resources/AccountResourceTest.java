package com.ntr1x.treasure.web.resources;

import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;

import org.junit.Assert;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
public class AccountResourceTest {

	@LocalServerPort
	private int port;
	
	@Test
    public void test() {
		
		WebTarget target = ClientBuilder
			.newClient()
			.target(String.format("http://localhost:%d", this.port))
		;
		
		{
			List<Account> response = target
	        	.path("/accounts")
				.queryParam("page", 0)
	        	.queryParam("size", 10)
	        	.request()
	        	.get(new GenericType<List<Account>>() {})
	        ;
	        
	        Assert.assertEquals(response.size(), 0);        
		}
	    
		Account user = null; {
	        
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
				.queryParam("page", 0)
	        	.queryParam("size", 10)
	        	.request()
	        	.get(Account.class)
	        ;
	        
	        Assert.assertEquals(r.getId(), user.getId());
		}
		
		{
			Account r = target
	        	.path(String.format("/accounts/%d", user.getId()))
				.queryParam("page", 0)
	        	.queryParam("size", 10)
	        	.request()
	        	.get(Account.class)
	        ;
	        
	        Assert.assertEquals(r.getId(), user.getId());
	        Assert.assertNull(r.get_action());
	        Assert.assertNull(r.getPassword());
	        Assert.assertNull(r.getPwdhash());
		}
    }
}
