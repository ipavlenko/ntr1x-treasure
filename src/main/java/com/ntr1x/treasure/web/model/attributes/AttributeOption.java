package com.ntr1x.treasure.web.model.attributes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "core_option")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeOption {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	@XmlElement
    @XmlInverseReference(mappedBy = "options")
	@ManyToOne
	@JoinColumn(name = "AttributeId", nullable = false, updatable = false)
	private AttributeEntity attribute;

	@Column(name = "Name", nullable = false)
	private String name;

	@Column(name = "Title", nullable = false)
	private String title;

	@Column(name = "Value", nullable = true)
	private String value;
}
