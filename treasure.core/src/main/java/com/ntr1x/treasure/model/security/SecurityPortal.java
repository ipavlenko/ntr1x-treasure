package com.ntr1x.treasure.model.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="security_portals")
@NamedQueries({
	@NamedQuery(
		name = "SecurityPortal.accessible",
		query = "SELECT p FROM SecurityPortal p"
	),
	@NamedQuery(
		name = "SecurityPortal.byName",
		query = "SELECT p FROM SecurityPortal p WHERE p.name = :name"
	)
})
public class SecurityPortal implements Serializable {

	private static final long serialVersionUID = -8259478674858449910L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@Column(name = "Name", nullable = false, unique = true)
	private String name;
	
	@Column(name = "Url", nullable = false, unique = true)
	private String url;
	
	@Column(name = "Title", nullable = false)
	private String title;
	
	@Column(name = "CookieDomain", nullable = false)
	private String cookieDomain;
	
	@Column(name = "CookieName", nullable = false)
	private String cookieName;
}
