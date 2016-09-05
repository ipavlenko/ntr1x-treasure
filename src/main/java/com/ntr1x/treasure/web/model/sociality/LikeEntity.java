package com.ntr1x.treasure.web.model.sociality;

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
@Table(name = "core_like")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class LikeEntity {

    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	@Column(name = "Rate", nullable = false)
	private int rate;

	@Column(name = "Message", nullable = false, columnDefinition = "MEDIUMTEXT")
	private String message;

	@XmlElement
    @XmlInverseReference(mappedBy = "likes")
	@ManyToOne
	@JoinColumn(name = "RelatesId", nullable = false, updatable = true)
	private SecurityResource relate;

	@ManyToOne
	@JoinColumn(name = "UserId", nullable = false, updatable = true)
	@XmlElement
	private SecurityUser user;
}
