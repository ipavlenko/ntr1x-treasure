package com.ntr1x.treasure.model.catalog;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.ntr1x.treasure.model.security.SecurityPortal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalog_offers_categories")
public class OfferCategoryEntity implements Serializable {
	
	private static final long serialVersionUID = -3738443247159306133L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;
	
	@Column(name = "Width", nullable = true)
	private Integer width;
	
	@Column(name = "Height", nullable = true)
	private Integer height;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;
	
	@Column(name = "Name", unique = true, nullable = false)
	private String name;
	
	@Column(name = "Title", nullable = false)
	private String title;
}
