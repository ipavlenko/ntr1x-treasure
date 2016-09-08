package com.ntr1x.treasure.web.oauth;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.ntr1x.treasure.web.model.Authenticator;
import com.ntr1x.treasure.web.oauth.IOAuthService.Credentials;

@Service
public class OAuth {

	@Inject
	private OAuth2GoogleFactory google;
	
	@Inject
	private OAuth2FacebookFactory facebook;
	
	@Inject
	private OAuth1TwitterFactory twitter;
	
	@PersistenceContext
	private EntityManager em;
	
	@Transactional
	public IOAuthService service(String name) {
		
		Authenticator settings = em.createNamedQuery("SecurityProvider.accessibleByName", Authenticator.class)
			.setParameter("name", name)
			.getSingleResult()
		;
		
		Credentials credentials = new Credentials(
			settings.getClientId(),
			settings.getClientSecret(),
			settings.getRedirectUri()
		);
		
		switch (name) {
			case "google": return google.create(credentials);
			case "facebook": return facebook.create(credentials);
			case "twitter": return twitter.create(credentials);
		}
		
		throw new IllegalArgumentException();
	}
}
