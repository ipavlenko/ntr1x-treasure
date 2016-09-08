package com.ntr1x.treasure.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "likes")
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@CascadeOnDelete
@Getter
@Setter
public class Like extends Resource {
	
	@XmlElement
	@XmlInverseReference(mappedBy = "likes")
	@ManyToOne
	@JoinColumn(name = "RelateId", nullable = false, updatable = false)
	private Resource relate;
	
	@XmlElement
	@ManyToOne
	@JoinColumn(name = "UserId", nullable = false, updatable = false)
	private User user;
	
	@Column(name = "Value")
	private int value;
	
	@Column(name = "Message", nullable = false, columnDefinition = "MEDIUMTEXT")
    private String message;
}
