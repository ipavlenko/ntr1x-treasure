package com.ntr1x.treasure.web.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.converter.AppConverterProvider.LocalDateTimeConverter;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "comments")
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Comment extends Resource {

	@XmlElement
	@XmlInverseReference(mappedBy = "comments")
	@ManyToOne
	@JoinColumn(name = "RelateId", nullable = false, updatable = false)
	private Resource relate;
	
	@XmlElement
	@ManyToOne
	@JoinColumn(name = "AccountId", nullable = false, updatable = false)
	private Account account;
	
	@Lob
	@Column(name = "Message")
	private String message;
		
	@Column(name = "Published")
	@XmlJavaTypeAdapter(LocalDateTimeConverter.class)
	@ApiModelProperty(example="1970-01-01T00:00:00")
	private LocalDateTime published;
}
