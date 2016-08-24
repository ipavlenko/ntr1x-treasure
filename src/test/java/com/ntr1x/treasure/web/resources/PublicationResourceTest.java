package com.ntr1x.treasure.web.resources;

import java.util.ArrayList;
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
import com.ntr1x.treasure.web.model.Action;
import com.ntr1x.treasure.web.model.Publication;
import com.ntr1x.treasure.web.model.Tag;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
public class PublicationResourceTest {

	@LocalServerPort
	private int port;
	
	@Test
    public void test() {
		
		WebTarget target = ClientBuilder
			.newClient()
			.target(String.format("http://localhost:%d", this.port))
		;
		
		{
			List<Publication> response = target
	        	.path("/publications")
				.queryParam("page", 0)
	        	.queryParam("size", 10)
	        	.request()
	        	.get(new GenericType<List<Publication>>() {})
	        ;
	        
	        Assert.assertEquals(response.size(), 0);
		}
	    
		{
	        Publication s = new Publication();
	        s.setTitle("Demo Publication");
	        s.setContent("Demo Publication Content");
	        s.set_action(Action.CREATE);
	        
	        Publication r = target
		    	.path("/publications")
		    	.request(MediaType.APPLICATION_JSON_TYPE)
		    	.post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Publication.class)
		    ;
	        
	        Assert.assertNotNull(r.getId());
	        Assert.assertEquals(s.getTitle(), r.getTitle());
	        Assert.assertEquals(s.getContent(), r.getContent());
	        Assert.assertNull(r.get_action());
		}
		
		{
			List<Publication> response = target
	        	.path("/publications")
				.queryParam("page", 0)
	        	.queryParam("size", 10)
	        	.request()
	        	.get(new GenericType<List<Publication>>() {})
	        ;
	        
	        Assert.assertEquals(response.size(), 1);
		}
		
		{
		    
		    
            Publication s = new Publication(); {
            
                s.setTitle("Demo Publication With Tags");
                s.setContent("Demo Publication Content");
                s.set_action(Action.CREATE);
                
                List<Tag> tags = new ArrayList<>();
                
                {
                    Tag tag = new Tag();
                    tag.set_action(Action.CREATE);
                    tag.setValue("Demo1");
                    tag.setRelate(s);
                    tags.add(tag);
                }
                
                {
                    Tag tag = new Tag();
                    tag.set_action(Action.CREATE);
                    tag.setValue("Demo2");
                    tag.setRelate(s);
                    tags.add(tag);
                }
            
                s.setTags(tags);
            }
            
            Publication r = target
                .path("/publications")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Publication.class)
            ;
            
            Assert.assertNotNull(r.getId());
            Assert.assertEquals(s.getTitle(), r.getTitle());
            Assert.assertEquals(s.getContent(), r.getContent());
            Assert.assertNull(r.get_action());
            Assert.assertEquals(s.getTags().size(), s.getTags().size());
        }
    }
}
