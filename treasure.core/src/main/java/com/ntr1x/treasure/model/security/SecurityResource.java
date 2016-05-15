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
	name = "security_resources",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = { "PortalId", "Name" })
	}
)
@NamedQueries({
	@NamedQuery(
		name = "SecurityResource.accessibleByName",
		query =
			  "	SELECT re"
			+ "	FROM SecurityResource re"
			+ "	WHERE re.portal.name = :portal"
			+ "	  AND re.name = :name"
	)
})
public class SecurityResource implements Serializable {
	
	private static final long serialVersionUID = 1435085801400266138L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;
	
	@Column(name = "Locked", nullable = false)
	private boolean locked;
	
	@Column(name = "Name", nullable = false)
	private String name;
}
