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
@Table(name = "offers")
@NamedQueries({
        @NamedQuery(
                name = "Offer.list",
                query =
                        "	SELECT o "
                        + "	FROM Offer o "
        ),
        @NamedQuery(
                name = "Offer.accessibleOfCategory",
                query =
                        "	SELECT o "
                        + "	FROM Offer o "
                        + " WHERE o.category.id = :category "
        )
})
public class Offer implements Serializable {

    private static final long serialVersionUID = 2554213245651636698L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CategoryId", nullable = false, updatable = true)
    private OfferCategory category;

    @Column(name = "Title", nullable = false)
    private String title;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "Description", nullable = true)
    private String description;

}