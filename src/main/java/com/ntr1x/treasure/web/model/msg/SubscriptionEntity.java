package com.ntr1x.treasure.web.model.msg;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ntr1x.treasure.web.model.purchase.GoodEntity;
import com.ntr1x.treasure.web.model.security.SecurityUser;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_comments")
public class SubscriptionEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@ManyToOne
	@JoinColumn(name = "GoodId", nullable = false, updatable = true)
	private GoodEntity good;

	@ManyToOne
	@JoinColumn(name = "UserId", nullable = false, updatable = true)
	private SecurityUser user;
}
