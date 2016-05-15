package com.ntr1x.treasure.model.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persons_categories")
@NamedQueries({
        @NamedQuery(
                name = "PersonCategory.accessible",
                query =
                        "	SELECT pc"
                        + "	FROM PersonCategory pc"
        ),
        @NamedQuery(
                name = "PersonCategory.accessibleByName",
                query =
                        "	SELECT pc"
                        + "	FROM PersonCategory pc"
                        + "	WHERE"
                        + "	  pc.name = :name"
        ),
        @NamedQuery(
                name = "PersonCategory.accessibleById",
                query =
                        "	SELECT pc"
                        + "	FROM PersonCategory pc"
                        + "	WHERE"
                        + "	  pc.id = :id"
        )
})
public class PersonCategory implements Serializable {

    private static final long serialVersionUID = 5463288617904550687L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;

    @Column(name = "Width", nullable = true)
    private Integer width;

    @Column(name = "Height", nullable = true)
    private Integer height;

    @Column(name = "Name", unique = true, nullable = false)
    private String name;

    @Column(name = "Title", nullable = false)
    private String title;
}
