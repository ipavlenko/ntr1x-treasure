package com.ntr1x.treasure.web.services;

import java.util.Map;

public interface IStoreMailService {
	
    void sendSignupConfirmation(String email, Map<String, Object> params);
	void sendPasswdInstructions(String email, Map<String, Object> params);
}
