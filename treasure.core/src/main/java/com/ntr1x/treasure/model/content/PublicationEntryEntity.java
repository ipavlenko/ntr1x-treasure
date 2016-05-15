package com.ntr1x.treasure.model.content;

import java.io.Serializable;
import java.time.LocalDate;

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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import com.ntr1x.treasure.model.security.SecurityPortal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "content_publications_entries")
@NamedQueries({
	@NamedQuery(
		name = "PublicationEntryEntity.accessible",
		query =
			  "	SELECT p"
			+ "	FROM PublicationEntryEntity p"
			+ "	WHERE p.portal.name = :portal"
			+ "	  AND p.category.id = :category"
	)
})
public class PublicationEntryEntity implements Serializable {

	private static final long serialVersionUID = -1503375444716731200L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "CategoryId", nullable = false, updatable = true)
	private PublicationCategoryEntity category;

	@Column(name = "Title", nullable = false)
	private String title;
	
	@Lob
	@Column(name = "Promo", nullable = false)
	private String promo;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "Description", nullable = true)
	private String description;
	
	@Column(name = "Date", nullable = true)
	private LocalDate date;
}
