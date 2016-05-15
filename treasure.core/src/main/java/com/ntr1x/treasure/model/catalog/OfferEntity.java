package com.ntr1x.treasure.model.catalog;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
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
@Table(name = "catalog_offers_entries")
public class OfferEntity implements Serializable {

	private static final long serialVersionUID = 8315563372102026366L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "ObjectId", nullable = true, updatable = true)
	private ObjectEntryEntity object;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "CategoryId", nullable = false, updatable = true)
	private OfferCategoryEntity category;
	
	@Column(name = "Title", nullable = false)
	private String title;
	
	@Lob
	@Column(name = "Promo", nullable = false)
	private String promo;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "Description", nullable = true)
	private String description;
}
