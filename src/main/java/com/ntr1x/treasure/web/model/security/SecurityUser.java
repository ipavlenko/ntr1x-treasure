package com.ntr1x.treasure.web.model.security;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.model.assets.DeliveryPlace;
import com.ntr1x.treasure.web.model.assets.PaymentMethod;
import com.ntr1x.treasure.web.model.purchase.CartEntity;
import com.ntr1x.treasure.web.model.purchase.ProviderEntity;
import com.ntr1x.treasure.web.model.purchase.PurchaseEntity;

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
@Table(
	name = "security_users",
	uniqueConstraints = {
		@UniqueConstraint(columnNames = {"Email" })
	}
)
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@PrimaryKeyJoinColumn(name="ResourceId", referencedColumnName="Id")
@CascadeOnDelete
public class SecurityUser extends SecurityResource {
	
    public static enum Role {
        USER,
        SELLER,
        DELIVERY,
        ADMIN,
        MODERATOR
    }

	@Column(name = "Confirmed", nullable = false)
	private boolean confirmed;
	
    @XmlTransient
    @Column(name = "Random")
    @ApiModelProperty(hidden = true)
    private Integer random;

    @XmlElement
    @XmlInverseReference(mappedBy = "user")
    @OneToOne(mappedBy = "user", cascade = { CascadeType.REMOVE })
    @ResourceRelation
    private CartEntity cart;
    
    
//	@Column(name = "Source", nullable = false)
//	private String source;
//
//	@Column(name = "Reference", nullable = false)
//	private String reference;

	@Column(name = "Date", nullable = false)
	private Date date;

	@Column(name = "Email", nullable = false)
	private String email;

    @Column(name = "Phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "UserName", nullable = false)
    private String userName;

    @Column(name = "Surname", nullable = false)
    private String surname;

    @Column(name = "MiddleName", nullable = true)
    private String middleName;

    @XmlTransient
	@Column(name = "PwdHash", nullable = true)
	@ApiModelProperty(hidden = true)
	private String pwdhash;

    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private Role role;

	@XmlElement
    @XmlInverseReference(mappedBy = "user")
	@OneToMany(mappedBy = "user")
	@CascadeOnDelete
	@ResourceRelation
	private List<ProviderEntity> providers;

	@XmlElement
    @XmlInverseReference(mappedBy = "user")
	@OneToMany(mappedBy = "user")
	@CascadeOnDelete
	@ResourceRelation
	private List<PurchaseEntity> purchases;

	@XmlElement
    @XmlInverseReference(mappedBy = "owner")
	@OneToMany(mappedBy = "owner")
	@CascadeOnDelete
	@ResourceRelation
	private List<PaymentMethod> paymentMethods;

	@XmlElement
    @XmlInverseReference(mappedBy = "owner")
	@OneToMany(mappedBy = "owner")
	@CascadeOnDelete
	@ResourceRelation
	private List<DeliveryPlace> deliveryPlaces;
}
