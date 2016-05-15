package com.ntr1x.treasure.model.security;

import java.io.Serializable;
import java.time.LocalDate;

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
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
	name = "security_users",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = { "PortalId", "Source", "Reference", "Email" })
	}
)
@NamedQueries({
	@NamedQuery(
		name = "SecurityUser.accessible",
		query =
			  "	SELECT su"
			+ "	FROM SecurityUser su"
			+ "	WHERE su.portal.id = :portal"
	),
	@NamedQuery(
		name = "SecurityUser.accessibleFiltered",
		query =
			  "	SELECT su"
			+ "	FROM SecurityUser su"
			+ "	WHERE su.portal.id = :portal"
			+ "	  AND (:confirmed IS NULL OR :confirmed = su.confirmed)"
			+ "	  AND (:locked IS NULL OR :locked = su.locked)"
			+ "	  AND (:admin IS NULL OR :admin = su.admin)"
		,
		hints = {
			@QueryHint(name = QueryHints.BIND_PARAMETERS, value = HintValues.TRUE)
		}
	),
	@NamedQuery(
		name = "SecurityUser.accessibleById",
		query =
			  "	SELECT su"
			+ "	FROM SecurityUser su"
			+ "	WHERE su.portal.id = :portal"
			+ "	  AND su.id = :id"
	),
	@NamedQuery(
		name = "SecurityUser.accessibleInternalByEmail",
		query =
			  "	SELECT su"
			+ "	FROM SecurityUser su"
			+ "	WHERE su.portal.id = :portal"
			+ "	  AND su.email = :email"
			+ "	  AND su.source = ''"
	),
	@NamedQuery(
		name = "SecurityUser.accessibleOAuthByEmail",
		query =
			  "	SELECT su"
			+ "	FROM SecurityUser su"
			+ "	WHERE su.portal.id = :portal"
			+ "	  AND su.source = :source"
			+ "	  AND su.reference = :reference"
	)
})
public class SecurityUser implements Serializable {
	
	private static final long serialVersionUID = 6254413798508888204L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;
	
	@Column(name = "Locked", nullable = false)
	private boolean locked;
	
	@Column(name = "Confirmed", nullable = false)
	private boolean confirmed;
	
	@Column(name = "Admin", nullable = false)
	private boolean admin;
	
	@Column(name = "Source", nullable = false)
	private String source;
	
	@Column(name = "Reference", nullable = false)
	private String reference;
	
	@Column(name = "Date", nullable = false)
	private LocalDate date;
	
	@Column(name = "Name", nullable = false)
	private String name;
	
	@Column(name = "Email", nullable = false)
	private String email;
	
	@Column(name = "PwdHash", nullable = true)
	private String pwdhash;
}
