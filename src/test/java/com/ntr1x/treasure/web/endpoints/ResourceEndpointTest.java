package com.ntr1x.treasure.web.endpoints;

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
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntr1x.treasure.web.App;
import com.ntr1x.treasure.web.model.p3.Publication;
import com.ntr1x.treasure.web.services.IProfilerService;
import com.ntr1x.treasure.web.services.IPublicationService;
import com.ntr1x.treasure.web.services.ISubscriptionService.SubscriptionMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-test.properties" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ResourceEndpointTest {

    @LocalServerPort
    private int port;
    
    @Inject
    private IProfilerService profiler;
    
    @Inject
    private ObjectMapper mapper;
    
    @Test
    public void test01() {
        
        WebTarget target = ClientBuilder
            .newClient()
            .target(String.format("http://localhost:%d", this.port))
        ;
        
        CountDownLatch latch = new CountDownLatch(3);
        
        StandardWebSocketClient client = new StandardWebSocketClient();
        
        WebSocketHandler handler = new TextWebSocketHandler() {
            
            @Override
            public void afterConnectionEstablished(WebSocketSession session) throws Exception {
                
                latch.countDown();
                
                session.sendMessage(
                    new TextMessage(
                        mapper.writeValueAsString(
                            new SubscriptionMessage(
                                SubscriptionMessage.Type.SUBSCRIBE,
                                "^/publications/?.*"
                            )
                        )
                    )
                );
                
                profiler.withDisabledSecurity(() -> {
                    
                    {
                        IPublicationService.PublicationCreate s = new IPublicationService.PublicationCreate(); {
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
                        IPublicationService.PublicationCreate s = new IPublicationService.PublicationCreate(); {
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
            String.format("ws://localhost:%d/socket/resources", port)
        );
        
        connectionManager.start();
        
        try {
            
            if (!latch.await(10, TimeUnit.SECONDS)) {
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
