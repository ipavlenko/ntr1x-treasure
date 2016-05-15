package com.ntr1x.treasure.model.catalog;
//package com.example.treasure.model.catalog;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//import java.io.Serializable;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(
//	name = "catalog_objects_fields",
//	uniqueConstraints = {
//		@UniqueConstraint(columnNames={"CategoryId", "Name"})
//	})
//
//@NamedQueries({
//	@NamedQuery(
//		name = "ObjectEntryField.accessibleByName",
//		query =
//			"	SELECT pc"
//			+ "	FROM ObjectEntryField pc"
//			+ "	WHERE pc.name = :name"
//	)
//})
//public class ObjectEntryField  implements Serializable {
//
//	private static final long serialVersionUID = -3154781281186467172L;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "Id")
//	private long id;
//
//	@ManyToOne(fetch = FetchType.EAGER, optional = false)
//	@JoinColumn(name = "CategoryId", nullable = false, updatable = true)
//	private ObjectCategoryEntity category;
//
//	@Column(name = "Name", nullable = false)
//	private String name;
//
//	@Column(name = "Title", nullable = false)
//	private String title;
//}
