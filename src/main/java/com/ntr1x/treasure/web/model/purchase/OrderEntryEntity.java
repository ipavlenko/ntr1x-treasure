package com.ntr1x.treasure.web.model.purchase;

import java.text.Collator;
import java.util.Comparator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_order_entries")
public class OrderEntryEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "OrderId", nullable = false, updatable = true)
	@XmlElement
	private OrderEntity order;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "GoodId", nullable = false, updatable = true)
	@XmlElement
	private GoodEntity good;

	@Column(name = "Quantity", nullable = false)
	private Float quantity;

	@Column(name = "Confirmed", nullable = false)
	private boolean confirmed;

	private static final Collator COLLATOR = Collator.getInstance();

	public static final Comparator<OrderEntryEntity> COMPARATOR = (o1, o2) -> {

		int res = COLLATOR.compare(
				o1.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("title")).findFirst().get().getValue(),
				o2.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("title")).findFirst().get().getValue()
		);

		if (res == 0) {
			res = COLLATOR.compare(
					o1.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("brand")).findFirst().get().getValue(),
					o2.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("brand")).findFirst().get().getValue()
			);
		}
		if (res == 0) {
			res = COLLATOR.compare(
					o1.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("desc")).findFirst().get().getValue(),
					o2.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("desc")).findFirst().get().getValue()
			);
		}
		if (res == 0) {
			res = COLLATOR.compare(
					o1.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("color")).findFirst().get().getValue(),
					o2.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("color")).findFirst().get().getValue()
			);
		}
		if (res == 0) {
			res = COLLATOR.compare(
					o1.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("size")).findFirst().get().getValue(),
					o2.getGood().getAttributes().stream().filter(a -> a.getAttribute().getName().equals("size")).findFirst().get().getValue()
			);
		}

		return res;
	};
}

