package com.ntr1x.treasure.web.model.purchase;

import lombok.*;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;
import java.text.Collator;
import java.util.Comparator;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_order_entries")
public class OrderEntryEntity implements Serializable{

	private static final long serialVersionUID = 7657110812175563337L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

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

