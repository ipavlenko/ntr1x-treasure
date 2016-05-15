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
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "media")
@NamedQueries({
    @NamedQuery(
        name = "Media.accessible",
        query =
                "	SELECT m "
              + "	FROM Media m "
//                      " SELECT * "
//                    + " FROM Media "
//                    + "     JOIN Upload ON Media.Upload.id = Upload.id "
        ),
    @NamedQuery(
        name = "Media.mediaOfCategory",
        query =
                "	SELECT m "
                + "	FROM Media m "
                + " WHERE m.category.id = :category "
    )
})
public class Media implements Serializable {

    private static final long serialVersionUID = -3920341337101514332L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "Category", nullable = false, updatable = true)
    private MediaCategory category;

    @Column(name = "Title", nullable = false)
    private String title;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "Description", nullable = true)
    private String description;

    @Column(name = "Published", nullable = true)
    private LocalDateTime published;

//    @XmlTransient
//    @OneToOne(fetch = FetchType.EAGER, optional = true)
//    @JoinColumn(name = "Upload", nullable = true, updatable = true)
//    private Upload upload;
}
