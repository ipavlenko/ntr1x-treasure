package com.ntr1x.treasure.web.model.p1;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p2.AttributeOption;

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
