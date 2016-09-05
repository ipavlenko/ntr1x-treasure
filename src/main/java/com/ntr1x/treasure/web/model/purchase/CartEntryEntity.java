package com.ntr1x.treasure.web.model.purchase;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

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
@Table(name = "core_cart_entries")
public class CartEntryEntity {

	public enum Type {
		CART,
		COMPARE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CartId", nullable = false, updatable = true)
	@XmlElement
	@XmlInverseReference(mappedBy="entries")
	private CartEntity cart;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "GoodId", nullable = false, updatable = true)
	@XmlElement
//	@XmlInverseReference(mappedBy="cartEntries")
	private GoodEntity good;

	@Column(name = "ResourceType", nullable = false)
	@Enumerated(EnumType.STRING)
	private Type type;

	@Column(name = "Quantity", nullable = false)
	private Float quantity;
}
