package com.ntr1x.treasure.web.model.p5;

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

import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p3.Order;
import com.ntr1x.treasure.web.model.p4.Modification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders_entries")
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class OrderEntry extends Resource {

	@ManyToOne
	@JoinColumn(name = "OrderId", nullable = false, updatable = true)
	@XmlElement
	private Order order;

	@ManyToOne
	@JoinColumn(name = "ModificationId", nullable = false, updatable = true)
	@XmlElement
	private Modification modification;

	@Column(name = "Quantity")
	private float quantity;

	@Column(name = "Confirmed")
	private boolean confirmed;
}

