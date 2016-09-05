package com.ntr1x.treasure.web.model.purchase;

import com.ntr1x.treasure.web.model.security.SecurityResource;
import com.ntr1x.treasure.web.model.security.SecurityUser;
import lombok.*;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@Getter
@Setter
//@EqualsAndHashCode(exclude = "purchases")
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_provider")
@ToString(exclude = {"user"})
@NamedQueries({
		@NamedQuery(
				name = "ProviderEntity.accessibleOfUserId",
				query =
						"	SELECT p"
								+ "	FROM ProviderEntity p"
								+ "	WHERE "
								+ "	  p.user.id = :id"
		),
		@NamedQuery(
				name = "ProviderEntity.accessibleOfIdUserId",
				query =
						"	SELECT p"
								+ "	FROM ProviderEntity p"
								+ "	WHERE "
								+ "	  p.user.id = :uid AND p.id = :pid"
		),
//		@NamedQuery(
//				name = "ProviderEntity.accessibleOfEmptyPurchases",
//				query =
//						"	SELECT p"
//								+ "	FROM ProviderEntity p"
//								+ "	JOIN PurchaseEntity p.purchases purch"
//								+ "	WHERE "
//								+ "	  p.user.id = :id AND purch IS NULL"
//		)
})
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@PrimaryKeyJoinColumn(name="ResourceId", referencedColumnName="Id")
public class ProviderEntity extends SecurityResource {

	public enum Status {
		NEW,
		OPEN
	}

	private static final long serialVersionUID = 8380657372416993368L;

	@Column(name = "Title", nullable = false)
	private String title;

	@Column(name = "Promo", nullable = false, columnDefinition = "VARCHAR(511)")
	private String promo;

	@Column(name = "Description", nullable = false, columnDefinition = "MEDIUMTEXT")
	private String desc;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "UserId", nullable = false)
	@XmlElement
	private SecurityUser user;

	@XmlElement
	@XmlInverseReference(mappedBy="provider")
	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "provider", orphanRemoval = true)
	private List<PurchaseEntity> purchases;
}
