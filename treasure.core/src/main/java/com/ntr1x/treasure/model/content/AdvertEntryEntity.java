package com.ntr1x.treasure.model.content;

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
@Table(name = "content_adverts_entries")
public class AdvertEntryEntity implements Serializable {
	
	private static final long serialVersionUID = -3558452611161026746L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@Column(name = "Url", nullable = false)
	private String url;
	
	@Column(name = "Title", nullable = false)
	private String title;
	
	@Lob
	@Column(name = "Promo", nullable = false)
	private String promo;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "Description", nullable = true)
	private String description;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "CategoryId", nullable = false, updatable = true)
	private AdvertCategoryEntity category;
}
