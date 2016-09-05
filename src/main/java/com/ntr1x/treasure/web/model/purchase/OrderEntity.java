package com.ntr1x.treasure.web.model.purchase;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import com.ntr1x.treasure.web.model.assets.DeliveryPlace;
import com.ntr1x.treasure.web.model.security.SecurityResource;
import com.ntr1x.treasure.web.model.security.SecurityUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_order")
@NamedQueries({
		@NamedQuery(
				name = "OrderEntity.accessibleId",
				query =
						"	SELECT o"
								+ "	FROM OrderEntity o"
								+ "	WHERE o.id = :id"
		),
		@NamedQuery(
				name = "OrderEntity.accessibleUserId",
				query =
						"	SELECT o"
								+ "	FROM OrderEntity o"
								+ "	INNER JOIN o.user u"
								+ "	WHERE u.id = :id"
		),
		@NamedQuery(
				name = "OrderEntity.accessibleUserIdAndStatus",
				query =
						"	SELECT o"
								+ "	FROM OrderEntity o"
								+ "	INNER JOIN o.user u"
								+ "	WHERE u.id = :id"
								+ "	AND (:status IS NULL OR o.status = :status) "
		),
		@NamedQuery(
				name = "OrderEntity.accessibleSellerId",
				query =
						"	SELECT o"
								+ "	FROM OrderEntity o"
								+ "	INNER JOIN o.seller s"
								+ "	WHERE s.id = :id"
		),
		@NamedQuery(
				name = "OrderEntity.accessibleSellerIdAndPurchaseId",
				query =
						"	SELECT o"
								+ "	FROM OrderEntity o "
								+ "	INNER JOIN o.entries entrs "
								+ "	INNER JOIN entrs.good.purchase p "
								+ "	WHERE o.seller.id = :id "
								+ "	AND p.id = :pid "
		),
		@NamedQuery(
				name = "OrderEntity.accessibleSellerIdAndPurchaseIdAndStatus",
				query =
						"	SELECT o"
								+ "	FROM OrderEntity o "
								+ "	INNER JOIN o.entries entrs "
								+ "	INNER JOIN entrs.good.purchase p "
								+ "	WHERE o.seller.id = :id "
								+ "	AND p.id = :pid "
								+ "	AND (:status IS NULL OR o.status = :status) "
		),
})
@PrimaryKeyJoinColumn(name="ResourceId", referencedColumnName="Id")
public class OrderEntity extends SecurityResource {

	private static final long serialVersionUID = -8308947880606353446L;

	public enum Status {
		NEW,					// Новый
		REQUIRES_PAYMENT,		// Требует оплаты (пользователь согласился с договором, выбрал способ доставки, селлер перевел поставку в оплату и заказ выкупился)
		CANCELED,				// Отменен (селлер перевел поставку в оплату, но заказ не выкупился)
		PAID,					// Оплачен
		CONFIRMED,				// Оплата подтверждена - селлер подтвердил получение оплаты
		READY,					// Готов к выдаче
		RECEIVED				// Получен
	}

//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "Id")
//	private long id;

//	@OneToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
//	@JoinColumn(name = "ResourceId", nullable = false, updatable = true)
//	private SecurityResource resource;

	@Column(name = "Status")
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "Date", nullable = false)
	private java.util.Date date;

	@Column(name = "PaidDate", nullable = true)
	private java.util.Date paidDate;

	@Column(name = "ConfirmDate", nullable = true)
	private java.util.Date confirmDate;

	@Column(name = "ReadyDate", nullable = true)
	private java.util.Date readyDate;

	@Column(name = "ReceivedDate", nullable = true)
	private java.util.Date receivedDate;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "UserId", nullable = false, updatable = true)
	private SecurityUser user;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PlaceId", nullable = false, updatable = true)
	private DeliveryPlace dPlace;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "SellerId", nullable = false, updatable = true)
	private SecurityUser seller;

	@OneToMany(fetch = FetchType.EAGER, targetEntity = OrderEntryEntity.class, mappedBy = "order", cascade = CascadeType.ALL)
	private List<OrderEntryEntity> entries;
}
