package com.ntr1x.treasure.web.model.catalog;


import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Lob;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

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
@Table(name = "catalog_company")
@PrimaryKeyJoinColumn(name = "ResourceId", referencedColumnName = "Id")
public class CompanyEntity extends SecurityResource {

	@Column(name = "Title", nullable = false)
	private String title;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "Description", nullable = true)
	private String description;
}
