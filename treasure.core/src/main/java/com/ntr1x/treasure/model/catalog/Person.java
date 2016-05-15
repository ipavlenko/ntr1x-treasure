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
@Table(name = "persons")
@NamedQueries({
        @NamedQuery(
                name = "Person.accessible",
                query =
                        "	SELECT p"
                        + "	FROM Person p"
        ),
        @NamedQuery(
                name = "Person.accessibleOfCategory",
                query =
                        "	SELECT p"
                        + "	FROM Person p"
                        + "	WHERE "
                        + "	  p.category.id = :category"
        )
})
public class Person implements Serializable {

    private static final long serialVersionUID = 3489503714412884198L;

    public enum Gender {
        MALE,
        FEMALE
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CategoryId", nullable = false, updatable = true)
    private PersonCategory category;

    @Column(name = "Gender", nullable = true)
    private Gender gender;

    @Column(name = "Name", nullable = false)
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "Description", nullable = true)
    private String description;
}
