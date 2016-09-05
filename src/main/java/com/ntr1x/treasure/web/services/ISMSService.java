package com.ntr1x.treasure.web.services;

import java.util.List;

public interface ISMSService {

    String sendSMS(String text, List<String> phones);

}
