package com.ntr1x.treasure.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(
	name = "grants",
	indexes= {
		@Index(columnList = "Pattern, Action", unique = true),
	}
)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@CascadeOnDelete
@Getter
@Setter
public class Grant extends Resource {
	
	@XmlElement
    @XmlInverseReference(mappedBy = "grants")
	@ManyToOne
	@JoinColumn(name = "AccountId", nullable = false, updatable = false)
	private User account;
	
	@Column(name = "Pattern")
	private String pattern;
	
	@Column(name = "Action")
    private String action;
}
