package com.ntr1x.treasure.model.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
	name = "security_providers",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = { "PortalId", "Name" })
	}
)
@NamedQueries({
	@NamedQuery(
		name = "SecurityProvider.accessible",
		query =
			  "	SELECT sp"
			+ "	FROM SecurityProvider sp"
			+ "	WHERE sp.portal.id = :portal"
	),
	@NamedQuery(
		name = "SecurityProvider.accessibleById",
		query =
			  "	SELECT sp"
			+ "	FROM SecurityProvider sp"
			+ "	WHERE sp.portal.id = :portal"
			+ "	 AND sp.id = :id"
	),
	@NamedQuery(
		name = "SecurityProvider.accessibleByName",
		query =
			  "	SELECT sp"
			+ "	FROM SecurityProvider sp"
			+ "	WHERE sp.portal.id = :portal"
			+ "	 AND sp.name = :name"
	),
})
public class SecurityProvider implements Serializable {
	
	private static final long serialVersionUID = -750213572692750029L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;
	
	@Column(name = "Name", nullable = false)
	private String name;
	
	@Column(name = "ClientId", nullable = false)
	private String clientId;
	
	@Column(name = "ClientSecret", nullable = false)
	private String clientSecret;
	
	@Column(name = "RedirectUri", nullable = false)
	private String redirectUri;
}
