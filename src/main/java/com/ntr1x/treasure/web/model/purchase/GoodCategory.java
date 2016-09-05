package com.ntr1x.treasure.web.model.purchase;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;

import org.eclipse.persistence.annotations.CascadeOnDelete;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;

import com.ntr1x.treasure.web.model.security.SecurityResource.ResourceRelation;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "core_good_categories")
@EqualsAndHashCode(exclude = "subcategories")
public class GoodCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Title", nullable = false)
    private String title;

	@XmlElement
    @XmlInverseReference(mappedBy = "parent")
	@OneToMany(mappedBy = "parent")
	@CascadeOnDelete
	@ResourceRelation
	private List<GoodCategory> subcategories;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ParentId", nullable = true, updatable = true)
	@XmlElement
	@XmlInverseReference(mappedBy="subcategories")
	private GoodCategory parent;
}
