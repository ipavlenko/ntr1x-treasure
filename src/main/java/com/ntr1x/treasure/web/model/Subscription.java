package com.ntr1x.treasure.web.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

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
public class Subscription {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private Long id;

	@XmlElement
    @XmlInverseReference(mappedBy = "subscribers")
    @ManyToOne
    @JoinColumn(name = "RelateId", nullable = false, updatable = false)
    private Resource relate;

	@XmlElement
    @XmlInverseReference(mappedBy = "subscriptions")
	@ManyToOne
	@JoinColumn(name = "UserId", nullable = false, updatable = false)
	private User user;
}
