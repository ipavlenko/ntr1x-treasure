package com.ntr1x.treasure.web.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
	name = "security_providers",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"Name"})
	}
)
@NamedQueries({
	@NamedQuery(
		name = "SecurityProvider.accessible",
		query =
			  "	SELECT sp"
			+ "	FROM SecurityProvider sp"
	),
	@NamedQuery(
		name = "SecurityProvider.accessibleById",
		query =
			  "	SELECT sp"
			+ "	FROM SecurityProvider sp"
			+ "	WHERE sp.id = :id"
	),
	@NamedQuery(
		name = "SecurityProvider.accessibleByName",
		query =
			  "	SELECT sp"
			+ "	FROM SecurityProvider sp"
			+ "	WHERE sp.name = :name"
	),
})
public class SecurityProvider implements Serializable {
	
	private static final long serialVersionUID = -750213572692750029L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@Column(name = "Name", nullable = false)
	private String name;
	
	@Column(name = "ClientId", nullable = false)
	private String clientId;
	
	@Column(name = "ClientSecret", nullable = false)
	private String clientSecret;
	
	@Column(name = "RedirectUri", nullable = false)
	private String redirectUri;
}
