package com.ntr1x.treasure.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

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
@Table(name = "carts_entries")
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class CartEntry extends Resource {

	public enum Type {
		CART,
		COMPARE
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CartId", nullable = false, updatable = true)
	@XmlElement
	@XmlInverseReference(mappedBy="entries")
	private Cart cart;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "GoodId", nullable = false, updatable = true)
	@XmlElement
//	@XmlInverseReference(mappedBy="cartEntries")
	private Modification modification;

	@Column(name = "Type", nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(name = "Quantity", nullable = false)
	private float quantity;
}
