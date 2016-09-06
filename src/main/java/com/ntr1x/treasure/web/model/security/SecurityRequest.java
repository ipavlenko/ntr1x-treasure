package com.ntr1x.treasure.web.model.security;

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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "security_requests")
@NamedQueries({
	@NamedQuery(
		name = "SecurityRequest.accessibleByToken",
		query =
			  "	SELECT sr"
			+ "	FROM SecurityRequest sr"
			+ "	WHERE sr.token = :token"
	)
})
public class SecurityRequest implements Serializable {

	private static final long serialVersionUID = 8017293223021675750L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;
	
	@Column(name = "Token", nullable = false, unique = true)
	private String token;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "ActionId", nullable = false, updatable = true)
	private SecurityAction action;
}
