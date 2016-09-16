package com.ntr1x.treasure.web.model.p2;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p1.Attribute;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "resources_attributes")
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ResourceAttribute extends Resource {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;
    
    @XmlElement
    @XmlInverseReference(mappedBy = "attributes")
    @ManyToOne
    @JoinColumn(name = "RelateId", nullable = false, updatable = false)
    private Resource relate;

    @XmlElement
    @XmlInverseReference(mappedBy = "values")
	@ManyToOne
	@JoinColumn(name = "AttributeId", nullable = false, updatable = false)
	private Attribute attribute;

	@Column(name = "Value", nullable = false, columnDefinition = "MEDIUMTEXT")
	private String value;
}
