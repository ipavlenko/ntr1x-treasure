package com.ntr1x.treasure.web.model.p5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p2.Cart;
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
@Table(name = "carts_entries")
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class CartEntry extends Resource {

	public enum Type {
		CART,
		COMPARE
	}

	@XmlElement
    @XmlInverseReference(mappedBy = "entries")
	@ManyToOne
	@JoinColumn(name = "CartId", nullable = false, updatable = true)
	private Cart cart;

	@XmlElement
    @XmlInverseReference(mappedBy = "carted")
    @ManyToOne
    @JoinColumn(name = "ModificationId", nullable = false, updatable = true)
    private Modification modification;
	
	@Column(name = "Type", nullable = false)
	private Type type;

	@Column(name = "Quantity", nullable = false)
	private float quantity;
}
