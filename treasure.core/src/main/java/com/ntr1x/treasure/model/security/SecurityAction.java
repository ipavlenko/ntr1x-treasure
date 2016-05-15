package com.ntr1x.treasure.model.security;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;
	
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
