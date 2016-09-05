package com.ntr1x.treasure.web.model.assets;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.model.security.SecurityResource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_image")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ImageEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@XmlElement
    @XmlInverseReference(mappedBy = "images")
    @ManyToOne
	@JoinColumn(name = "RelatesId", nullable = true, updatable = true)		//TODO
	private SecurityResource relate;

	@Column(name = "Title", nullable = false)
	private String title;
}
