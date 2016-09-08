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
import javax.xml.bind.annotation.XmlElement;
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
@Table(name = "orders")
@PrimaryKeyJoinColumn(name="ResourceId", referencedColumnName="Id")
public class Order extends Resource {

	public enum Status {
		NEW,					// Новый
		REQUIRES_PAYMENT,		// Требует оплаты (пользователь согласился с договором, выбрал способ доставки, селлер перевел поставку в оплату и заказ выкупился)
		CANCELED,				// Отменен (селлер перевел поставку в оплату, но заказ не выкупился)
		PAID,					// Оплачен
		CONFIRMED,				// Оплата подтверждена - селлер подтвердил получение оплаты
		READY,					// Готов к выдаче
		RECEIVED				// Получен
	}

	@Column(name = "Status")
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "Created")
	@XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    @ApiModelProperty(example="1970-01-01T00:00:00")
    private LocalDateTime created;
	
	@Column(name = "Paid")
    @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    @ApiModelProperty(example="1970-01-01T00:00:00")
    private LocalDateTime paid;

	@Column(name = "Confirmed")
    @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    @ApiModelProperty(example="1970-01-01T00:00:00")
    private LocalDateTime confirmed;

	@Column(name = "Ready")
    @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    @ApiModelProperty(example="1970-01-01T00:00:00")
    private LocalDateTime ready;

	@Column(name = "Received")
    @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    @ApiModelProperty(example="1970-01-01T00:00:00")
    private LocalDateTime received;

	@ManyToOne
	@JoinColumn(name = "UserId", nullable = false, updatable = true)
	private User user;

	@XmlElement
    @XmlInverseReference(mappedBy = "orders")
	@ManyToOne
	@JoinColumn(name = "DepotId", nullable = false, updatable = true)
	private Depot depot;

	@XmlElement
    @XmlInverseReference(mappedBy = "orders")
	@ManyToOne
	@JoinColumn(name = "PurchaseId", nullable = false, updatable = true)
	private Purchase purchase;

	@XmlElement
    @XmlInverseReference(mappedBy = "order")
	@ResourceRelation
	@OneToMany(mappedBy = "order")
	@CascadeOnDelete
    @ApiModelProperty(hidden = true)
	private List<OrderEntry> entries;
}
