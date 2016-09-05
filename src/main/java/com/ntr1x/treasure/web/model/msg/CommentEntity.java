package com.ntr1x.treasure.web.model.msg;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "core_comments")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CommentEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	@Column(name = "Confidential", nullable = false)
	private boolean confidential;

	@Column(name = "Moderated", nullable = false)
	private boolean moderated;

	@ManyToOne
	@JoinColumn(name = "RelatesId", nullable = false, updatable = true)
	@XmlElement
	@XmlInverseReference(mappedBy="comments")
	private SecurityResource relate;

	@ManyToOne
	@JoinColumn(name = "UserId", nullable = false, updatable = true)
	private SecurityUser user;

	@Lob
	@Column(name = "Body", nullable = false)
	private String body;
}
