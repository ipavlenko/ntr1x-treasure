package com.ntr1x.treasure.web.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.converter.AppConverterProvider.LocalDateTimeConverter;

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
@Table(name = "purchases")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
public class Purchase extends Resource {

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

	@Column(name = "OpenDate")
    @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    @ApiModelProperty(example="1970-01-01T00:00:00")
    private LocalDateTime openDate;

	@Column(name = "StopDate")
    @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    @ApiModelProperty(example="1970-01-01T00:00:00")
    private LocalDateTime stopDate;

	@Column(name = "StartDeliveryDate")
    @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    @ApiModelProperty(example="1970-01-01T00:00:00")
    private LocalDateTime startDeliveryDate;
	
	@Column(name = "NextDeliveryExpectDate")
    @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    @ApiModelProperty(example="1970-01-01T00:00:00")
    private LocalDateTime nextDeliveryExpectDate;
	
	@ManyToOne
	@JoinColumn(name = "ProviderId", nullable = true, updatable = true)
	@XmlElement
    @XmlInverseReference(mappedBy = "purchases")
	private Provider provider;

	@ManyToOne
	@JoinColumn(name = "MethodId", nullable = false, updatable = true)
	@XmlElement
	private Method method;

    @ManyToOne
    @JoinColumn(name = "UserId", nullable = false)
    @XmlElement
    @XmlInverseReference(mappedBy="purchases")
    private User user;

    @XmlElement
    @XmlInverseReference(mappedBy = "purchase")
    @OneToMany(mappedBy = "purchase")
    @CascadeOnDelete
    @ApiModelProperty(hidden = true)
    private List<Good> goods;
}
