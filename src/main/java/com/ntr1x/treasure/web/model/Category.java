package com.ntr1x.treasure.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "categories")
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Category extends Resource {

	@XmlElement
	@XmlInverseReference(mappedBy = "subcategories")
	@ManyToOne
	@JoinColumn(name = "RelateId", nullable = true, updatable = false)
	private Resource relate;
	
	@Column(name = "Title")
	private String title;
	
	@Lob
	@Column(name = "Description")
	private String description;
}
