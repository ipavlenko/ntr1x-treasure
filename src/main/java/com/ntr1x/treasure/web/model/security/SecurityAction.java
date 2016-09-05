package com.ntr1x.treasure.web.model.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "security_actions")
public class SecurityAction implements Serializable {

	private static final long serialVersionUID = -8747230100147297884L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@Column(name = "Name", nullable = false)
	private String name;
	
	@Column(name = "Signature", nullable = false)
	private int signature;
	
	@Column(name = "DateTime", nullable = false)
	private LocalDateTime dateTime;
	
	@Lob
	@Basic(fetch = FetchType.LAZY, optional = true)
	@Column(name = "Data", nullable = true)
	private Serializable data;
}
