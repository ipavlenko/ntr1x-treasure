package com.ntr1x.treasure.web.model.security;

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
@Table(name = "security_tokens")
@NamedQueries({
		@NamedQuery(
				name = "Token.accessibleOfUserId",
				query =
						"	SELECT t"
								+ "	FROM Token t"
								+ "	WHERE "
								+ "	  t.user.id = :uid"
		)
})
public class Token {

	public static final int SIGNUP = 1 << 0;
	public static final int PASSWD = 1 << 1;

	public static final int ACTION = 1 << 2;
	public static final int PURCHASE = 1 << 3;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "UserId", nullable = false, updatable = true)
	private SecurityUser user;

	@Column(name = "Type", nullable = false)
	private int type;

	@Column(name = "Token", nullable = false)
	private int token;
}
