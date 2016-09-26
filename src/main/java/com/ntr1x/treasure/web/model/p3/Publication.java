package com.ntr1x.treasure.web.model.p3;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.ntr1x.treasure.web.converter.AppConverterProvider.LocalDateTimeConverter;
import com.ntr1x.treasure.web.model.p0.Resource;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "publications", indexes= { @Index(columnList="Published") })
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class Publication extends Resource {

	@Column(name = "Title")
	private String title;
	
	@Lob
	@Column(name = "Promo")
	private String promo;
	
	@Lob
	@Column(name = "Content")
	private String content;
	
	@Column(name = "Published")
	@XmlJavaTypeAdapter(LocalDateTimeConverter.class)
	@ApiModelProperty(example="1970-01-01T00:00:00")
	private LocalDateTime published;
}
