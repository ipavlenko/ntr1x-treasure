package com.ntr1x.treasure.model.catalog;

import java.io.Serializable;
import java.util.List;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.ntr1x.treasure.model.security.SecurityPortal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "catalog_objects_entries")
@NamedQueries({
	@NamedQuery(
		name = "ObjectEntryEntity.accessible",
		query =
			  "	SELECT p"
			+ "	FROM ObjectEntryEntity p"
			+ "	WHERE p.portal.name = :portal"
			+ "	  AND p.category.id = :category"
	)
})
public class ObjectEntryEntity implements Serializable {

	private static final long serialVersionUID = 8319481572048503732L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "PortalId", nullable = false, updatable = true)
	private SecurityPortal portal;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "CategoryId", nullable = false, updatable = true)
	private ObjectCategoryEntity category;

	@Column(name = "Title", nullable = false)
	private String title;

	@Lob
	@Column(name = "Promo", nullable = false)
	private String promo;

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(name = "Description", nullable = true)
	private String description;

	@OneToMany(fetch = FetchType.LAZY, targetEntity = OfferEntity.class, mappedBy = "object")
	private List<OfferEntity> offers;

	/*
	@ElementCollection(fetch = FetchType.EAGER, targetClass = ObjectEntryProp.class)
//	@MapKey(name="field.name")
//	@MapKey(name="field.getName()")
//	@MapKeyColumn(name = "field.name_key")
	@MapKeyColumn(name = "field.name")
	@CollectionTable(name="catalog_objects_props", joinColumns = {
		@JoinColumn(name = "ObjectId")
	})
	private Map<String, ObjectEntryProp> properties = new HashMap<>();
	*/
}
