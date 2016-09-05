//package com.ntr1x.treasure.web.resources;
//
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.List;
//
//import javax.inject.Inject;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.GenericType;
//import javax.ws.rs.core.MediaType;
//
//import org.junit.Assert;
//import org.junit.Before;
//import org.junit.FixMethodOrder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.junit.runners.MethodSorters;
//import org.springframework.boot.context.embedded.LocalServerPort;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.ntr1x.treasure.web.App;
//import com.ntr1x.treasure.web.model.Category;
//import com.ntr1x.treasure.web.model.Publication;
//import com.ntr1x.treasure.web.resources.CategoryResource.CategoryCreate;
//import com.ntr1x.treasure.web.resources.PublicationResource.PublicationCreate;
//import com.ntr1x.treasure.web.services.IProfilerService;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-test.properties" })
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
//public class PublicationResourceTest {
//
//	@LocalServerPort
//	private int port;
//	
//	@Inject
//    private IProfilerService profiler;
//    
//    private WebTarget target;
//    
//    @Before
//    public void init() {
//        
//        target = ClientBuilder
//            .newClient()
//            .target(String.format("http://localhost:%d", this.port))
//        ;
//    }
//	
//	@Test
//    public void test01() {
//		
//	    profiler.clearSearchIndex();
//	    
//	    profiler.withDisabledSecurity(() -> {
//	    
//	        List<Category> categories = new ArrayList<>(); {
//	            
//    	        for (String title : new String[] { "Артроскопия", "Энтопротезирование", "Травма", "Нейрохирургия" }) {
//                    
//                    CategoryCreate s = new CategoryCreate(); {
//                        s.title = title;
//                        s.aspects = new String[] { "test1" };
//                    }
//                    
//                    Category r = target
//                        .path("/categories")
//                        .request(MediaType.APPLICATION_JSON_TYPE)
//                        .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Category.class)
//                    ;
//                    
//                    categories.add(r);
//                    
//                    Assert.assertNotNull(r.getId());
//                }
//	        }
//	        
//	        Category c0 = categories.get(0);
//	        Category c1 = categories.get(1);
//	        Category c2 = categories.get(2);
//	        
//	        List<PublicationCreate> publications = new ArrayList<>(); {
//    		    
//	            {
//	                
//        	        PublicationCreate p = new PublicationCreate(); {
//        	            
//            	        p.title = "Первый пример";
//            	        p.promo = "Короткий текст примера";
//            	        p.content = "Полный текст примера";
//            	        p.published = LocalDateTime.now();
//            	        p.categories = new PublicationCreate.Category[] {
//        	                new PublicationCreate.Category(c0.getId()),
//        	                new PublicationCreate.Category(c1.getId()),
//            	        };
//        	        }
//        	        
//        	        publications.add(p);
//	            }
//	            
//	            {
//                    PublicationCreate p = new PublicationCreate(); {
//                        
//                        p.title = "Второй пример новости";
//                        p.promo = "Короткий текст второго примера новости";
//                        p.content = "Полный текст второго примера";
//                        p.published = LocalDateTime.now();
//                        p.categories = new PublicationCreate.Category[] {
//                            new PublicationCreate.Category(c1.getId()),
//                            new PublicationCreate.Category(c2.getId()),
//                        };
//                    }
//                    
//                    publications.add(p);
//                }
//	            
//	            {
//                    PublicationCreate p = new PublicationCreate(); {
//                        
//                        p.title = "Третий пример новости";
//                        p.promo = "Короткий текст третьего примера новости";
//                        p.content = "Полный текст третьего примера новости";
//                        p.published = LocalDateTime.now();
//                        p.categories = new PublicationCreate.Category[] {
//                            new PublicationCreate.Category(c0.getId()),
//                            new PublicationCreate.Category(c2.getId()),
//                        };
//                    }
//                    
//                    publications.add(p);
//                }
//    		}
//	        
//	        
//	        for (PublicationCreate p : publications) {
//    		    
//    		    Publication r = target
//                    .path("/publications")
//                    .request(MediaType.APPLICATION_JSON_TYPE)
//                    .post(Entity.entity(p, MediaType.APPLICATION_JSON_TYPE), Publication.class)
//                ;
//                
//                Assert.assertNotNull(r.getId());
//                Assert.assertEquals(p.title, r.getTitle());
//                Assert.assertEquals(p.promo, r.getPromo());
//                Assert.assertEquals(p.content, r.getContent());
////                Assert.assertEquals(p.published, r.getPublished());
//	        }
//	        
//	        {
//	            List<Publication> response = target
//	                .path("/publications")
//	                .queryParam("page", 0)
//	                .queryParam("size", 10)
//	                .request()
//	                .get(new GenericType<List<Publication>>() {})
//	            ;
//	            
//	            Assert.assertEquals(response.size(), 3);
//	        }
//
//	        
//	        {
//	            List<Publication> response = target
//	                .path("/publications/query")
//	                .queryParam("query", "Первый")
//	                .queryParam("page", 0)
//	                .queryParam("size", 10)
//	                .request()
//	                .get(new GenericType<List<Publication>>() {})
//	            ;
//	            
//	            Assert.assertEquals(1, response.size());
//	        }
//	        
//	        {
//	            List<Publication> response = target
//	                .path("/publications/query")
//	                .queryParam("query", "Новость")
//	                .queryParam("page", 0)
//	                .queryParam("size", 10)
//	                .request()
//	                .get(new GenericType<List<Publication>>() {})
//	            ;
//	            
//	            Assert.assertEquals(2, response.size());
//	        }
//	        
//	        {
//	            List<Publication> response = target
//	                .path("/publications/query")
//	                .queryParam("query", "новости")
//	                .queryParam("page", 0)
//	                .queryParam("size", 10)
//	                .request()
//	                .get(new GenericType<List<Publication>>() {})
//	            ;
//	            
//	            Assert.assertEquals(2, response.size());
//	        }
//	        
//	        {
//	            List<Publication> response = target
//	                .path("/publications/query")
//	                .queryParam("query", "Пример")
//	                .queryParam("page", 0)
//	                .queryParam("size", 10)
//	                .request()
//	                .get(new GenericType<List<Publication>>() {})
//	            ;
//	            
//	            Assert.assertEquals(3, response.size());
//	        }
//	        
//	        {
//	            List<Publication> response = target
//	                .path("/publications/query")
//	                .queryParam("query", "Blablabla")
//	                .queryParam("page", 0)
//	                .queryParam("size", 10)
//	                .request()
//	                .get(new GenericType<List<Publication>>() {})
//	            ;
//	            
//	            Assert.assertEquals(0, response.size());
//	        }
//	        
//	        {
//	            List<Publication> response = target
//	                .path("/publications/query")
//	                .queryParam("query", "пример")
//	                .queryParam("category", c0.getId())
//	                .queryParam("page", 0)
//	                .queryParam("size", 10)
//	                .request()
//	                .get(new GenericType<List<Publication>>() {})
//	            ;
//	            
//	            Assert.assertEquals(2, response.size());
//	        }
//	        
//	        {
//                List<Publication> response = target
//                    .path("/publications/query")
//                    .queryParam("category", c0.getId(), c1.getId(), c2.getId())
//                    .queryParam("page", 0)
//                    .queryParam("size", 10)
//                    .request()
//                    .get(new GenericType<List<Publication>>() {})
//                ;
//                
//                Assert.assertEquals(0, response.size());
//            }
//	        
//	        {
//                List<Publication> response = target
//                    .path("/publications/query")
//                    .queryParam("category", c1.getId(), c2.getId())
//                    .queryParam("page", 0)
//                    .queryParam("size", 10)
//                    .request()
//                    .get(new GenericType<List<Publication>>() {})
//                ;
//                
//                Assert.assertEquals(1, response.size());
//            }
//	        
//	        {
//                List<Publication> response = target
//                    .path("/publications/query")
//                    .queryParam("query", "")
//                    .queryParam("category", c2.getId())
//                    .queryParam("page", 0)
//                    .queryParam("size", 10)
//                    .request()
//                    .get(new GenericType<List<Publication>>() {})
//                ;
//                
//                Assert.assertEquals(2, response.size());
//            }
//	        
//	        {
//                List<Publication> response = target
//                    .path("/publications/query")
//                    .queryParam("query", "*")
//                    .queryParam("category", c2.getId())
//                    .queryParam("page", 0)
//                    .queryParam("size", 10)
//                    .request()
//                    .get(new GenericType<List<Publication>>() {})
//                ;
//                
//                Assert.assertEquals(2, response.size());
//            }
//	    });
//    }
//}
