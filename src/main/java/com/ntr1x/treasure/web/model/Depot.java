package com.ntr1x.treasure.web.model;

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
@Table(name = "depots")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class Depot extends Resource {

	@ManyToOne
	@JoinColumn(name = "UserId", nullable = false)
	@XmlElement
    @XmlInverseReference(mappedBy = "depots")
	private User user;
	
	@Column(name = "DeliveryPrice")
	private float deliveryPrice;
	
	@XmlElement
    @XmlInverseReference(mappedBy = "depot")
    @OneToMany(mappedBy = "depot")
    @CascadeOnDelete
    @ApiModelProperty(hidden = true)
	private List<Order> orders;
}
