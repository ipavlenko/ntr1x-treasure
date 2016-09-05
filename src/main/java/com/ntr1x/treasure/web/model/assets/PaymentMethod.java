package com.ntr1x.treasure.web.model.assets;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.model.purchase.StoreAction;
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
@Table(name = "assets_payment_method")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@PrimaryKeyJoinColumn(name="ResourceId", referencedColumnName="Id")
public class PaymentMethod extends SecurityResource {

    @XmlElement
    @XmlInverseReference(mappedBy = "paymentMethods")
	@ManyToOne
	@JoinColumn(name = "UserId", nullable = false)
	private SecurityUser owner;

	@Transient
	private StoreAction action;
}
