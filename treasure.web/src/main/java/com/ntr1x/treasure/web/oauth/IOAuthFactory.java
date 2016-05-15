package com.ntr1x.treasure.web.oauth;

import com.ntr1x.treasure.web.oauth.IOAuthService.Credentials;

public interface IOAuthFactory {

	IOAuthService create(Credentials credentials);
}
