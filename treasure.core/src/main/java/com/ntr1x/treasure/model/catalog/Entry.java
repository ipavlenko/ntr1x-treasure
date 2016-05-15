package com.ntr1x.treasure.model.catalog;

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
@Table(name = "entries")
@NamedQueries({
        @NamedQuery(
                name = "Entry.accessible",
                query =
                        "	SELECT p"
                        + "	FROM Entry p"
                        + "	WHERE p.category.id = :category"
        )
})
public class Entry implements Serializable {

    private static final long serialVersionUID = 2668123842021795579L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CategoryId", nullable = false, updatable = true)
    private EntryCategory category;

    @Column(name = "Price", nullable = false)
    private double price;

    @Column(name = "Title", nullable = false)
    private String title;

    @Lob
    @Column(name = "Promo", nullable = false)
    private String promo;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "Description", nullable = true)
    private String description;

//    @OneToMany(fetch = FetchType.LAZY, targetEntity = OfferEntity.class, mappedBy = "object")
//    private List<Offer> offers;

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
