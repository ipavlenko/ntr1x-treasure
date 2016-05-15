package com.ntr1x.treasure.model.catalog;

import com.ntr1x.treasure.model.security.SecurityPortal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalog_persons_entries")
@NamedQueries({
	@NamedQuery(
		name = "PersonEntryEntity.accessible",
		query =
			  "	SELECT p"
			+ "	FROM PersonEntryEntity p"
			+ "	WHERE p.portal.id = :portal"
			+ "	  AND p.category.id = :category"
	)
})
public class PersonEntryEntity implements Serializable {
	
	private static final long serialVersionUID = 3234455991601819444L;
	
	public enum Gender {
		MALE,
		FEMALE
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "CategoryId", nullable = false, updatable = true)
	private PersonCategoryEntity category;

	@Column(name = "Gender", nullable = true)
	private Gender gender;
	
	@Column(name = "Name", nullable = false)
	private String name;
	
	@Lob
	@Column(name = "Promo", nullable = false)
	private String promo;
	
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "Description", nullable = true)
	private String description;
}
