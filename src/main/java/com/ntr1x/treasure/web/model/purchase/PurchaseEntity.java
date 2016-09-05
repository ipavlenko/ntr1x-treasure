package com.ntr1x.treasure.web.model.purchase;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.model.assets.PaymentMethod;
import com.ntr1x.treasure.web.model.security.SecurityResource;
import com.ntr1x.treasure.web.model.security.SecurityUser;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_purchase")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@PrimaryKeyJoinColumn(name="ResourceId", referencedColumnName="Id")
public class PurchaseEntity extends SecurityResource {

	public enum Status {
		NEW,				//  Новая (селлер создает поставку, указав поставщика и сроки проведения поставки, поставка отправляется на модерацию)
		MODERATION,			//  Требует доработки (модератор по какой-то причине не одобрил поставку и потребовал её доработки)
		APPROVED,			//  Одобрена модератором
		OPEN,				// + Открыта (появляется в общем списке поставок, можно редактировать товары)
		HIDDEN,				// Скрыта (скрывается из общего списка поставок, можно редактировать товары)
		STOPED,				// Стоп (скрывается из общего списка поставок, селлер формирует и отправляет заказ поставщику, после получения счета от поставщика селлер проставляет выкупившиеся позиции и переводит поставку в Оплату)
		PAYMENT,			// + - Оплата (участники оплачивают заказ; селлер получает груз от поставщика
		DISTRIBUTION,		// + - Раздача (покупатель приходит в центр выдачи и забирает свой заказ)
		FINISHED			// + - (планируемая дата) Завершена (все заказы разданы, все проблемы решены)
	}

	@Column(name = "Title", nullable = false)
	private String title;

	@Column(name = "Status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "OpenDate", nullable = true)
	private java.util.Date openDate;

	@Column(name = "StopDate", nullable = true)
	private java.util.Date stopDate;

	@Column(name = "StartDeliveryDate", nullable = true)
	private java.util.Date startDeliveryDate;

	@Column(name = "NextDeliveryExpectDate", nullable = true)
	private java.util.Date nextDeliveryExpectDate;

	@ManyToOne(fetch = FetchType.LAZY)
//	@ManyToOne()
	@JoinColumn(name = "RelatesId", nullable = true, updatable = true)
	@XmlElement
	@XmlInverseReference(mappedBy="purchases")
	private ProviderEntity provider;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "MethodId", nullable = false, updatable = true)
	@XmlElement
	private PaymentMethod paymentMethod;

    @ManyToOne()
    @JoinColumn(name = "UserId", nullable = false)
    @XmlElement
//    @XmlInverseReference(mappedBy="purchases")
    private SecurityUser user;

    @XmlElement
    @XmlInverseReference(mappedBy = "purchase")
    @OneToMany(mappedBy = "purchase")
    @CascadeOnDelete
    private List<GoodEntity> goods;

	@Transient
	private StoreAction action;
}
