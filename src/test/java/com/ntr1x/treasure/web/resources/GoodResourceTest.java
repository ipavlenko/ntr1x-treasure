//package com.ntr1x.treasure.web.resources;
//
//import java.util.List;
//
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.core.GenericType;
//
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.context.embedded.LocalServerPort;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.ntr1x.treasure.web.App;
//import com.ntr1x.treasure.web.model.Good;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
//@TestPropertySource(locations="classpath:application-test.properties")
//public class GoodResourceTest {
//
//	@LocalServerPort
//	private int port;
//	
//	@Test
//    public void test() {
//		
//		List<Good> response = ClientBuilder.newClient()
//			.target(String.format("http://localhost:%d", this.port))
//        	.path("/goods")
//			.queryParam("page", 0)
//        	.queryParam("size", 10)
//        	.request()
//        	.get(new GenericType<List<Good>>() {})
//        ;
//        
//        Assert.assertEquals(response.size(), 0);
//    }
//}
