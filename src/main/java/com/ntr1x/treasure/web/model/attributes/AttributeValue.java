package com.ntr1x.treasure.web.model.attributes;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.model.purchase.StoreAction;
import com.ntr1x.treasure.web.model.security.SecurityResource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_attribute_value")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AttributeValue {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;
    
    @XmlElement
    @XmlInverseReference(mappedBy = "attributes")
    @ManyToOne
    @JoinColumn(name = "RelatesId", nullable = false, updatable = false)
    private SecurityResource relate;

    @XmlElement
    @XmlInverseReference(mappedBy = "values")
	@ManyToOne
	@JoinColumn(name = "AttributeId")
    @OrderBy("order ASC")
	private AttributeEntity attribute;

	@Column(name = "Value", nullable = false, columnDefinition = "MEDIUMTEXT")
	private String value;

	@Transient
	private StoreAction action;
}
