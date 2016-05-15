package com.ntr1x.treasure.model.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media_categories")
@NamedQueries({
        @NamedQuery(
                name = "MediaCategory.list",
                query =
                        "	SELECT pc"
                        + "	FROM MediaCategory pc "
        ),
        @NamedQuery(
                name = "MediaCategory.accessibleByName",
                query =
                    "	SELECT pc "
                    + "	FROM MediaCategory pc "
                    + "	WHERE pc.name = :name "
        )
})
public class MediaCategory implements Serializable {

    private static final long serialVersionUID = -6757134094435979639L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Title", nullable = false)
    private String title;

    @Column(name = "Width", nullable = true)
    private Integer width;

    @Column(name = "Height", nullable = true)
    private Integer height;

}
