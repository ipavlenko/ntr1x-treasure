package com.ntr1x.treasure.web.model.p1;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.converter.AppConverterProvider.LocalDateTimeConverter;
import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p2.Cart;
import com.ntr1x.treasure.web.model.p2.Depot;
import com.ntr1x.treasure.web.model.p2.Grant;
import com.ntr1x.treasure.web.model.p2.Method;
import com.ntr1x.treasure.web.model.p2.Provider;
import com.ntr1x.treasure.web.model.p2.Purchase;
import com.ntr1x.treasure.web.model.p2.Session;
import com.ntr1x.treasure.web.model.p2.Token;

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
	name = "users",
	indexes= {
		@Index(columnList = "Email", unique = true),
	}
)
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@CascadeOnDelete
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User extends Resource {
	
    public static enum Role {
        USER,
        SELLER,
        DELIVERY,
        ADMIN,
        MODERATOR
    }
    
    @Column(name = "Role")
    @Enumerated(EnumType.STRING)
    private Role role;
    
	@Column(name = "Email")
	private String email;

	@XmlTransient
	@Column(name = "Random")
	@ApiModelProperty(hidden = true)
	private Integer random;
	
	@Column(name = "Published")
	@XmlJavaTypeAdapter(LocalDateTimeConverter.class)
	@ApiModelProperty(example="1970-01-01T00:00:00")
	private LocalDateTime published;
	
	@XmlTransient
	@Column(name = "Pwdhash")
	@ApiModelProperty(hidden = true)
	private String pwdhash;
	
	@Column(name = "Phone", unique = true, nullable = false)
    private String phone;

    @Column(name = "UserName", nullable = false)
    private String name;

    @Column(name = "Surname", nullable = false)
    private String surname;

    @Column(name = "MiddleName", nullable = true)
    private String middlename;
    
    @Column(name = "Confirmed")
    private boolean confirmed;
	
    @Column(name = "Locked")
    private boolean locked;
    
    @Column(name = "Registered")
    @XmlJavaTypeAdapter(LocalDateTimeConverter.class)
    @ApiModelProperty(example="1970-01-01T00:00:00")
    private LocalDateTime registered;
    
	@XmlElement
    @XmlInverseReference(mappedBy = "user")
    @OneToOne(mappedBy = "user")
    @ResourceRelation
    private Cart cart;
	
	@ResourceRelation
    @XmlElement
    @XmlInverseReference(mappedBy = "user")
    @OneToMany(mappedBy = "user")
    @CascadeOnDelete
    @ApiModelProperty(hidden = true)
    private List<Grant> grants;
	
	@XmlElement
    @XmlInverseReference(mappedBy = "user")
    @OneToMany(mappedBy = "user")
    @CascadeOnDelete
    @ResourceRelation
    @ApiModelProperty(hidden = true)
    private List<Provider> providers;

    @XmlElement
    @XmlInverseReference(mappedBy = "user")
    @OneToMany(mappedBy = "user")
    @CascadeOnDelete
    @ResourceRelation
    @ApiModelProperty(hidden = true)
    private List<Purchase> purchases;

    @XmlElement
    @XmlInverseReference(mappedBy = "user")
    @OneToMany(mappedBy = "user")
    @CascadeOnDelete
    @ResourceRelation
    @ApiModelProperty(hidden = true)
    private List<Method> methods;

    @ResourceRelation
    @XmlElement
    @XmlInverseReference(mappedBy = "user")
    @OneToMany(mappedBy = "user")
    @CascadeOnDelete
    @ApiModelProperty(hidden = true)
    private List<Depot> depots;
	
	@ResourceRelation
	@XmlElement
	@XmlInverseReference(mappedBy = "user")
	@OneToMany(mappedBy = "user")
	@CascadeOnDelete
	@ApiModelProperty(hidden = true)
	private List<Session> sessions;
	
	@ResourceRelation
    @XmlElement
    @XmlInverseReference(mappedBy = "user")
    @OneToMany(mappedBy = "user")
    @CascadeOnDelete
    @ApiModelProperty(hidden = true)
    private List<Token> tokens;
	
//	@ResourceRelation
//    @XmlElement
//    @XmlInverseReference(mappedBy = "user")
//    @OneToMany(mappedBy = "user")
//    @CascadeOnDelete
//    @ApiModelProperty(hidden = true)
//    private List<Grant> grants;
}
