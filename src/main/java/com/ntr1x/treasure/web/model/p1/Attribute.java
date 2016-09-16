package com.ntr1x.treasure.web.model.p1;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
import com.ntr1x.treasure.web.model.p2.ResourceAttribute;

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

    @Column(name = "Title", nullable = false)
    private String title;
    
	@Column(name = "Filter", nullable = false)
	private boolean filter;

    @Column(name = "`Order`")
    private int order;

    @ResourceRelation
    @XmlElement
    @XmlInverseReference(mappedBy = "attribute")
    @OneToMany(mappedBy = "attribute")
    @CascadeOnDelete
    private List<ResourceAttribute> values;
    
    @XmlElement
    @XmlInverseReference(mappedBy = "attribute")
    @OneToMany(mappedBy = "attribute", fetch = FetchType.EAGER)
    @CascadeOnDelete
    private List<AttributeOption> options;
}
