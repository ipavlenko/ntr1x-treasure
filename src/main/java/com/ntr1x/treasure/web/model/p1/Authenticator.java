package com.ntr1x.treasure.web.model.p1;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
	name = "authenticators",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = { "Name" })
	}
)
public class Authenticator {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	@Column(name = "Name", nullable = false)
	private String name;
	
	@Column(name = "ClientId", nullable = false)
	private String clientId;
	
	@Column(name = "ClientSecret", nullable = false)
	private String clientSecret;
	
	@Column(name = "RedirectUri", nullable = false)
	private String redirectUri;
}
