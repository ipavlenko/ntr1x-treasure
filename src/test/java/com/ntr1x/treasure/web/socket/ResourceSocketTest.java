package com.ntr1x.treasure.web.socket;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.ntr1x.treasure.web.App;
import com.ntr1x.treasure.web.model.Publication;
import com.ntr1x.treasure.web.resources.PublicationResource.PublicationCreate;
import com.ntr1x.treasure.web.services.IProfilerService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:application-test.properties")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResourceSocketTest {

    @LocalServerPort
    private int port;
    
    @Inject
    private IProfilerService profiler;
    
    @Test
    public void init() {
        
        WebTarget target = ClientBuilder
            .newClient()
            .target(String.format("http://localhost:%d", this.port))
        ;
        
        CountDownLatch latch = new CountDownLatch(2);
        
        StandardWebSocketClient client = new StandardWebSocketClient();
        
        WebSocketHandler handler = new TextWebSocketHandler() {
            
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                
                latch.countDown();
                
                profiler.withDisabledSecurity(() -> {
                    
                    {
                        PublicationCreate s = new PublicationCreate(); {
                            s.title = "Demo Publication";
                            s.promo = "Demo Publication Promo";
                            s.content = "Demo Publication Promo";
                        }
                        
                        /*Publication r = */target
                            .path("/publications")
                            .request(MediaType.APPLICATION_JSON_TYPE)
                            .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Publication.class)
                        ;
                    }
                    
                    {
                        PublicationCreate s = new PublicationCreate(); {
                            s.title = "Demo Publication";
                            s.promo = "Demo Publication Promo";
                            s.content = "Demo Publication Promo";
                        }
                        
                        /*Publication r = */target
                            .path("/publications")
                            .request(MediaType.APPLICATION_JSON_TYPE)
                            .post(Entity.entity(s, MediaType.APPLICATION_JSON_TYPE), Publication.class)
                        ;
                    }
                });
            }
            
            public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
                
                System.out.println(message);
                latch.countDown();
            }
        };
        
        WebSocketConnectionManager connectionManager = new WebSocketConnectionManager(
            client,
            handler,
            String.format("ws://localhost:%d/socket", port)
        );
        
        connectionManager.start();
        
        try {
            
            if (!latch.await(50, TimeUnit.SECONDS)) {
                Assert.fail("Socket connection timeout");
            }
            
        } catch (InterruptedException e) {
            Thread.interrupted();
        }
        
        connectionManager.stop();

//        TextMessage message = messages.poll(WAIT_TIMEOUT, TimeUnit.SECONDS);
//        Assert.assertEquals(new TextMessage("connected"), message);
    }
    
//    @After
//    public void destroy() {
//    }
}
