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
@Table(name = "security_sessions")
public class SecuritySession implements Serializable {
	
	private static final long serialVersionUID = -6536992352642963536L;

    public static final String COOKIE = "VSTORE-Session";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "UserId", nullable = false, updatable = true)
	private SecurityUser user;

	@Column(name = "Signature", nullable = false)
	private int signature;
}
