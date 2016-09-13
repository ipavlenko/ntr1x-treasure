package com.ntr1x.treasure.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;

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
public class OrderEntry extends Resource {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "OrderId", nullable = false, updatable = true)
	@XmlElement
	private Order order;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "ModificationId", nullable = false, updatable = true)
	@XmlElement
	private Modification modification;

	@Column(name = "Quantity")
	private float quantity;

	@Column(name = "Confirmed")
	private boolean confirmed;

//	private static final Collator COLLATOR = Collator.getInstance();
//
//	public static final Comparator<OrderEntryEntity> COMPARATOR = (o1, o2) -> {
//
//		int res = COLLATOR.compare(
//				o1.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("title")).findFirst().get().getValue(),
//				o2.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("title")).findFirst().get().getValue()
//		);
//
//		if (res == 0) {
//			res = COLLATOR.compare(
//					o1.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("brand")).findFirst().get().getValue(),
//					o2.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("brand")).findFirst().get().getValue()
//			);
//		}
//		if (res == 0) {
//			res = COLLATOR.compare(
//					o1.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("desc")).findFirst().get().getValue(),
//					o2.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("desc")).findFirst().get().getValue()
//			);
//		}
//		if (res == 0) {
//			res = COLLATOR.compare(
//					o1.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("color")).findFirst().get().getValue(),
//					o2.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("color")).findFirst().get().getValue()
//			);
//		}
//		if (res == 0) {
//			res = COLLATOR.compare(
//					o1.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("size")).findFirst().get().getValue(),
//					o2.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("size")).findFirst().get().getValue()
//			);
//		}
//
//		return res;
//	};
}

