package com.ntr1x.treasure.web.services;

import java.util.List;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.moxy.xml.MoxyXmlFeature;
import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.utils.ConversionUtils;

@Service
public class SMSService implements ISMSService {

    private String senderPhone  = "+79514634088";
    private String authId       = "MAY2ZIMGM5YJEWZWU3NT";
    private String authToken    = "YmM4NTE0MWUwYjFkNzkxMGQ0NjQzNWQzMDFlOWI2";

    private String baseurl      = "https://api.plivo.com/v1/Account/";
    private String plivoResource= "/Message/";

    @Override
    public String sendSMS(String text, List<String> phones) {

        StringBuilder strPhones = new StringBuilder();
        for(String phone: phones) {
            strPhones.append(phone);
            strPhones.append("<");
        }
        if (!phones.isEmpty()) {
            strPhones.deleteCharAt(strPhones.length() -1);
        }

        String json = String.format("{\"src\":\"%s\", \"dst\":\"%s\", \"text\":\"%s\"}", senderPhone, strPhones, text);

        return ClientBuilder.newClient()
                .register(MoxyXmlFeature.class)
                .register(MoxyJsonFeature.class)
                .target(String.format("%s%s%s", baseurl, authId, plivoResource))
                .request(MediaType.APPLICATION_JSON)
////                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, authId)
////                .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, authToken)
                .header("Authorization", "Basic " + ConversionUtils.BASE64.encode((authId + ":" + authToken).getBytes()))
                .post(
                        Entity.entity(
                                json,
                                MediaType.APPLICATION_JSON
                        ),
                        String.class
                );
    }
}
