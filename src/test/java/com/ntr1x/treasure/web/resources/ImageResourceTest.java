package com.ntr1x.treasure.web.resources;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.glassfish.jersey.media.multipart.MultiPart;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.media.multipart.file.FileDataBodyPart;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
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
import com.ntr1x.treasure.web.converter.AppConverterProvider;
import com.ntr1x.treasure.web.converter.ImageSettingsProvider.ImageSettings;
import com.ntr1x.treasure.web.converter.ImageSettingsProvider.ImageSettingsConverter;
import com.ntr1x.treasure.web.model.p1.Image;
import com.ntr1x.treasure.web.model.p1.User;
import com.ntr1x.treasure.web.services.IProfilerService;
import com.ntr1x.treasure.web.services.IScaleImageService;
import com.ntr1x.treasure.web.services.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = { "classpath:application.properties", "classpath:application-test.properties" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ImageResourceTest {

    @LocalServerPort
    private int port;
    
    @Inject
    private IProfilerService profiler;
    
    private WebTarget target;
    
    @Before
    public void init() {
        
        target = ClientBuilder
            .newClient()
            .register(AppConverterProvider.class)
            .register(MoxyJsonFeature.class)
            .register(MoxyJsonFeature.class)
            .register(MultiPartFeature.class)
            .register(new LoggingFeature(Logger.getLogger(getClass().getName()), Level.INFO, null, null))
            .target(String.format("http://localhost:%d", this.port))
        ;
    }
    
    @Test
    public void test() {
        
        User[] users = { null };
        
        profiler.withDisabledSecurity(() -> {
            
            {
                IUserService.CreateUser s = new IUserService.CreateUser(); {
                    
                    s.role = User.Role.USER;
                    s.confirmed = true;
                    s.email = "user-upload@example.com";
                    s.password = "user-upload";
                    s.phone = "user-upload-phone";
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
        
        profiler.withCredentials(target, "user-upload@example.com", "user-upload", (token) -> {
            
            {
                ImageSettings settings = new ImageSettings(); {
                    settings.items = new ImageSettings.Item[] {
                        new ImageSettings.Item("cover-240x100", "png", 240, 100, IScaleImageService.Type.COVER),
                        new ImageSettings.Item("cover-100x240", "png", 100, 240, IScaleImageService.Type.COVER),
                        new ImageSettings.Item("cover-240x240", "png", 240, 240, IScaleImageService.Type.COVER),
                        new ImageSettings.Item("cover-240xauto", "png", 240, -1, IScaleImageService.Type.COVER),
                        new ImageSettings.Item("cover-autox240", "png", -1, 240, IScaleImageService.Type.COVER),
                        new ImageSettings.Item("cover-autoxauto", "png", -1, -1, IScaleImageService.Type.COVER),
                        
                        new ImageSettings.Item("contain-240x100", "png", 240, 100, IScaleImageService.Type.CONTAIN),
                        new ImageSettings.Item("contain-100x240", "png", 100, 240, IScaleImageService.Type.CONTAIN),
                        new ImageSettings.Item("contain-240x240", "png", 240, 240, IScaleImageService.Type.CONTAIN),
                        new ImageSettings.Item("contain-240xauto", "png", 240, -1, IScaleImageService.Type.CONTAIN),
                        new ImageSettings.Item("contain-autox240", "png", -1, 240, IScaleImageService.Type.CONTAIN),
                        new ImageSettings.Item("contain-autoxauto", "png", -1, -1, IScaleImageService.Type.CONTAIN),
                        
                        new ImageSettings.Item("scale-240x100", "png", 240, 100, IScaleImageService.Type.SCALE),
                        new ImageSettings.Item("scale-100x240", "png", 100, 240, IScaleImageService.Type.SCALE),
                        new ImageSettings.Item("scale-240x240", "png", 240, 240, IScaleImageService.Type.SCALE),
                        new ImageSettings.Item("scale-240xauto", "png", 240, -1, IScaleImageService.Type.SCALE),
                        new ImageSettings.Item("scale-autox240", "png", -1, 240, IScaleImageService.Type.SCALE),
                        new ImageSettings.Item("scale-autoxauto", "png", -1, -1, IScaleImageService.Type.SCALE),
                    };
                }
                
                System.out.println(new ImageSettingsConverter().toString(settings));
                
                @SuppressWarnings("resource")
                MultiPart mp = new FormDataMultiPart()
                    .field("settings", settings, MediaType.APPLICATION_JSON_TYPE)
                    .bodyPart(new FileDataBodyPart(
                        "file",
                        new File(getClass().getClassLoader().getResource("image.png").getFile()),
                        MediaType.APPLICATION_OCTET_STREAM_TYPE
                    ))
                ;
                
                Image image = target
                    .path("/images/m2m")
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .header(HttpHeaders.AUTHORIZATION, token)
                    .post(Entity.entity(mp, mp.getMediaType()), Image.class)
                ;
                
                Assert.assertNotNull(image.getId());
            }
        });
        
        profiler.withDisabledSecurity(() -> {
            
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
