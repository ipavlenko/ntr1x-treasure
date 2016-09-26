package com.ntr1x.treasure.web.model.p1;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.model.p0.Resource;
import com.ntr1x.treasure.web.model.p2.Grant;
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
	
	@Column(name = "Email")
	private String email;

	@XmlTransient
	@Column(name = "Random")
	@ApiModelProperty(hidden = true)
	private Integer random;
	
	@XmlTransient
	@Column(name = "Pwdhash")
	@ApiModelProperty(hidden = true)
	private String pwdhash;
	
	@Column(name = "Confirmed")
    private boolean confirmed;
    
    @Column(name = "Locked")
    private boolean locked;
	    
	@ResourceRelation
    @XmlElement
    @XmlInverseReference(mappedBy = "user")
    @OneToMany(mappedBy = "user")
    @CascadeOnDelete
    @ApiModelProperty(hidden = true)
    private List<Grant> grants;
		
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
}
