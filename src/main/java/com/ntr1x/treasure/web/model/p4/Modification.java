package com.ntr1x.treasure.web.model.p4;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
import com.ntr1x.treasure.web.model.p3.Good;
import com.ntr1x.treasure.web.model.p5.CartEntry;
import com.ntr1x.treasure.web.model.p5.OrderEntry;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "modifications")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class Modification extends Resource {

    @XmlElement
    @XmlInverseReference(mappedBy = "modifications")
    @ManyToOne
    @JoinColumn(name = "GoodId", nullable = false, updatable = false)
    private Good good;
        
    @Column(name = "Price", nullable = false)
    private float price;

    @Column(name = "Quantity", nullable = false)
    private float quantity;

    @Column(name = "SizeRange", nullable = false)
    private float sizeRange;
    
    @ResourceRelation
    @XmlElement
    @XmlInverseReference(mappedBy = "modification")
    @OneToMany(mappedBy = "modification")
    @CascadeOnDelete
    @ApiModelProperty(hidden = true)
    private List<OrderEntry> ordered;
    
    @ResourceRelation
    @XmlElement
    @XmlInverseReference(mappedBy = "modification")
    @OneToMany(mappedBy = "modification")
    @CascadeOnDelete
    @ApiModelProperty(hidden = true)
    private List<CartEntry> carted;
}
