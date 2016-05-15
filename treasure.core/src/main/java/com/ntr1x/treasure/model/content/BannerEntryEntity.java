package com.ntr1x.treasure.model.content;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import com.ntr1x.treasure.model.security.SecurityPortal;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalog_banners_entries")
@NamedQueries({
		@NamedQuery(
				name = "BannerEntryEntity.accessible",
				query =
					"	SELECT p"
					+ "	FROM BannerEntryEntity p"
					+ "	WHERE p.portal.name = :portal"
					+ "	  AND p.category.id = :category"
		)
})
public class BannerEntryEntity implements Serializable {

	private static final long serialVersionUID = 1488227770063031518L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "CategoryId", nullable = false, updatable = true)
	private BannerCategoryEntity category;

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
