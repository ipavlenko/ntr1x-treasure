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
@Table(name = "entries_categories")
@NamedQueries({
        @NamedQuery(
                name = "EntryCategory.accessible",
                query =
                        "	SELECT pc"
                        + "	FROM EntryCategory pc"
        ),
        @NamedQuery(
                name = "EntryCategory.accessibleByName",
                query =
                        "	SELECT pc"
                        + "	FROM EntryCategory pc"
                        + "	WHERE pc.name = :name"
        )
})
public class EntryCategory implements Serializable {

    private static final long serialVersionUID = -2270440728677958955L;

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
