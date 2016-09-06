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
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
	name = "security_privileges",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = { "UserId", "Pattern", "Action" })
	}
)
@NamedQueries({
	@NamedQuery(
		name = "SecurityPrivilege.check",
		query =
			  "	SELECT COUNT(up.id)"
			+ "	FROM"
			+ "		SecurityPrivilege up,"
			+ "		SecurityResource re"
			+ "	WHERE up.action = :action"
			+ "	  AND up.user.id = :user"
			+ "	  AND LOCATE(up.pattern, :resource) = 1"
	)
})
public class SecurityPrivilege implements Serializable {
	
	private static final long serialVersionUID = 6018126495464619220L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false/*, cascade = CascadeType.PERSIST*/)
	@JoinColumn(name = "UserId", nullable = false, insertable = false, updatable = false)
	private SecurityUser user;
	
	@Column(name = "Pattern")
	private String pattern;
	
	@Column(name = "Action", nullable = false)
	private String action;
}
