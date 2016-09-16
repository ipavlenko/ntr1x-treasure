package com.ntr1x.treasure.web.resources;

import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import com.ntr1x.treasure.web.services.ISMSService;

import io.swagger.annotations.Api;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Api("Security")
@Component
@Path("/sms")
public class SMSResource {

    @Inject
    private ISMSService sms;

    @NoArgsConstructor
    @AllArgsConstructor
    @XmlRootElement
    public static class SMSSendSettings {
        
        public String text;
        public List<String> phones;
    }

    @POST
    @Path("/send")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response sendSMS(SMSSendSettings settings) {

        // SET TEXT LIMIT FOR SMS
        if (settings.text.length() > 160) {
            throw new IllegalStateException("SMS text is too long. Maximum length of text is 160 symbols");
        }

        return Response.ok(
            sms.sendSMS(
                settings.text,
                settings.phones
            )
        ).build();
    }

}
