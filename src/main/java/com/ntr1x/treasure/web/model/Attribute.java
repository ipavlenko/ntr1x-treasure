package com.ntr1x.treasure.web.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "attributes")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class Attribute extends Resource {

	public enum Type {
		STRING,
		INTEGER,
        FLOAT
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	@Column(name = "ResourceType", nullable = true)
	@Enumerated(EnumType.STRING)
	private Type type;

    @Column(name = "Defining", nullable = false)
    private boolean defining;

	@Column(name = "Filtering", nullable = false)
	private boolean filtering;

    @Column(name = "ListOrder")
    private int order;

    @Column(name = "Name", nullable = false, unique = true)
    private String name;

	@Column(name = "Title", nullable = false)
	private String title;

    @XmlElement
    @XmlInverseReference(mappedBy = "attribute")
    @OneToMany(mappedBy = "attribute")
    @CascadeOnDelete
    @ResourceRelation
    private List<AttributeOption> options;
}
