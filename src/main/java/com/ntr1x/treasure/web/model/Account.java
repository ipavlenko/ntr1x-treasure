//package com.ntr1x.treasure.web.model;
//
//import java.util.List;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Index;
//import javax.persistence.OneToMany;
//import javax.persistence.PrimaryKeyJoinColumn;
//import javax.persistence.Table;
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//import javax.xml.bind.annotation.XmlTransient;
//
//import org.eclipse.persistence.annotations.CascadeOnDelete;
//import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
//
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Getter;
//import lombok.Setter;
//
//@Entity
//@Table(
//	name = "accounts",
//	indexes= {
//		@Index(columnList = "Email", unique = true),
//	}
//)
//@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
//@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
//@Getter
//@Setter
//public class Account extends Resource {
//	
//	@Column(name = "Email")
//	private String email;
//
//	@XmlTransient
//	@Column(name = "Random")
//	@ApiModelProperty(hidden = true)
//	private int random;
//	
//	@XmlTransient
//	@Column(name = "Pwdhash")
//	@ApiModelProperty(hidden = true)
//	private String pwdhash;
//	
//	@ResourceRelation
//	@XmlElement
//	@XmlInverseReference(mappedBy = "account")
//	@OneToMany(mappedBy = "account")
//	@CascadeOnDelete
//	@ApiModelProperty(hidden = true)
//	private List<Session> sessions;
//	
//	@ResourceRelation
//    @XmlElement
//    @XmlInverseReference(mappedBy = "account")
//    @OneToMany(mappedBy = "account")
//    @CascadeOnDelete
//    @ApiModelProperty(hidden = true)
//    private List<Grant> grants;
//}
