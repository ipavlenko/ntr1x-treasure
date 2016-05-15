package com.ntr1x.treasure.model.content;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
@Table(name = "content_publications_categories")
@NamedQueries({
	@NamedQuery(
		name = "PublicationCategoryEntity.accessible",
		query =
			  "	SELECT pc"
			+ "	FROM PublicationCategoryEntity pc"
			+ "	WHERE pc.portal.name = :portal"
	),
	@NamedQuery(
		name = "PublicationCategoryEntity.accessibleByName",
		query =
			  "	SELECT pc"
			+ "	FROM PublicationCategoryEntity pc"
			+ "	WHERE pc.portal.name = :portal"
			+ "	  AND pc.name = :name"
	)
})
public class PublicationCategoryEntity implements Serializable {
	
	private static final long serialVersionUID = 8265349322158571730L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;

	@Column(name = "Name", unique = true, nullable = false)
	private String name;
	
	@Column(name = "Title", nullable = false)
	private String title;
}
