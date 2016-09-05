package com.ntr1x.treasure.web.model.purchase;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.model.catalog.CompanyEntity;
import com.ntr1x.treasure.web.model.security.SecurityResource;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_good")
@NamedQueries({
		@NamedQuery(
				name = "GoodEntity.accessible",
				query =
						"	SELECT g "
						+ "	FROM GoodEntity g "
		),
		@NamedQuery(
				name = "GoodEntity.accessibleOfId",
				query =
						"	SELECT g "
						+ "	FROM GoodEntity g "
						+ " WHERE g.id = :id"
		),
		@NamedQuery(
				name = "GoodEntity.accessibleOfPurchaseId",
				query =
						"	SELECT g "
								+ "	FROM GoodEntity g "
								+ " WHERE g.purchase.id = :id"
		),
		@NamedQuery(
				name = "GoodEntity.accessibleOfResType",
				query =
						"	SELECT g"
								+ "	FROM GoodEntity g "
								+ "	WHERE "
								+ "	g.resType = :resType "
		)
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@PrimaryKeyJoinColumn(name="ResourceId", referencedColumnName="Id")
@ToString(exclude = {"purchase"})
public class GoodEntity extends SecurityResource {

	public enum Status {
		DELETED,
		ACTIVE
	}

	@Column(name = "Status", nullable = false)
	@Enumerated(EnumType.STRING)
	private Status status;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "CompanyId", nullable = true, updatable = true)		//TODO nullable = false
	private CompanyEntity company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PurchaseId", nullable = true, updatable = true)
    @XmlElement
    @XmlInverseReference(mappedBy="goods")
    private PurchaseEntity purchase;

	@Column(name = "Title", nullable = false)
	private String title;

	@Column(name = "Price", nullable = false)
	private Float price;

	@Column(name = "Quantity", nullable = false)
	private Float quantity;

	@Column(name = "SizeRange", nullable = false)
	private Float sizeRange = 0f;

//	@XmlElement
//	@XmlInverseReference(mappedBy="good")
//	@OneToMany(cascade = CascadeType.ALL, mappedBy = "good", orphanRemoval = true, fetch = FetchType.LAZY)					//TODO убрать эту ссылку
//	private List<CartEntryEntity> cartEntries;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "core_good_categories_map",
        joinColumns = @JoinColumn(name = "GoodId"),
        inverseJoinColumns = @JoinColumn(name = "CategoryId")
    )
    private List<GoodCategory> categories;

	@Transient
	private StoreAction action;
}
