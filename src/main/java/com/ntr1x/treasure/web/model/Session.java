package com.ntr1x.treasure.web.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "sessions")
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Session extends Resource {
    
    @XmlElement
    @XmlInverseReference(mappedBy = "sessions")
    @ManyToOne
    @JoinColumn(name = "AccountId", nullable = false, updatable = false)
    private Account account;
    
    @XmlTransient
    @Column(name = "Signature")
    @ApiModelProperty(hidden = true)
    private int signature;
}
