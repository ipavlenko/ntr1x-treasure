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
@Table(name = "catalog_banners_categories")
@NamedQueries({
		@NamedQuery(
				name = "BannerCategoryEntity.accessible",
				query =
					"	SELECT pc"
					+ "	FROM BannerCategoryEntity pc"
					+ "	WHERE pc.portal.name = :portal"
		),
		@NamedQuery(
				name = "BannerCategoryEntity.accessibleByName",
				query =
					"	SELECT pc"
					+ "	FROM BannerCategoryEntity pc"
					+ "	WHERE pc.portal.name = :portal"
					+ "	  AND pc.name = :name"
		)
})
public class BannerCategoryEntity implements Serializable {

	private static final long serialVersionUID = 5257332295175733276L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@Column (name = "Width", nullable = true)
	private Integer width;

	@Column (name = "Height", nullable = true)
	private Integer height;

	@ManyToOne (fetch = FetchType.EAGER, optional = false)
	@JoinColumn (name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;

	@Column (name = "Name", unique = true, nullable = false)
	private String name;

	@Column(name = "Title", nullable = false)
	private String title;
}
