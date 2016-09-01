package com.ntr1x.treasure.web.resources;

import java.util.List;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
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
import com.ntr1x.treasure.web.model.Category;
import com.ntr1x.treasure.web.resources.CategoryResource.CategoryCreate;
import com.ntr1x.treasure.web.services.IProfilerService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryResourceTest {

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
        
        profiler.withDisabledSecurity(() -> {
            
            for (String title : new String[] { "Артроскопия", "Энтопротезирование", "Травма", "Нейрохирургия" }) {
                
                CategoryCreate s = new CategoryCreate(); {
                    s.title = title;
                    s.aspects = new String[] { "directory" };
                }
                
                Category r = target
                    .path("/categories")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
                ;
                
                Assert.assertNotNull(r.getId());
                Assert.assertNotNull(r.getAspects());
                Assert.assertTrue(r.getAspects().contains("directory"));
            }
            
            List<Category> response = target
                .path("/categories")
                .queryParam("page", 0)
                .queryParam("size", 10)
                .request()
                .get(new GenericType<List<Category>>() {})
            ;
            
            Assert.assertEquals(response.size(), 4);
        });
    }
}
