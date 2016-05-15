package com.ntr1x.treasure.web.oauth;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.ntr1x.treasure.model.security.SecurityPortal;
import com.ntr1x.treasure.model.security.SecurityProvider;
import com.ntr1x.treasure.web.oauth.IOAuthService.Credentials;
import com.ntr1x.treasure.web.providers.UserElementFilter.RequestAttribute;

@ApplicationScoped
public class OAuth {
	
	@Inject @RequestAttribute
	private SecurityPortal portal;
	
	@Inject
	private OAuth2GoogleFactory google;
	
	@Inject
	private OAuth2FacebookFactory facebook;
	
	@Inject
	private OAuth1TwitterFactory twitter;
	
	@PersistenceContext(unitName="treasure.core")
	private EntityManager em;
	
	@Transactional
	public IOAuthService service(String name) {
		
		SecurityProvider settings = em.createNamedQuery("SecurityProvider.accessibleByName", SecurityProvider.class)
			.setParameter("portal", portal.getId())
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
