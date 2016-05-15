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
//		name = "catalog_objects_props",
//		uniqueConstraints = {
//				@UniqueConstraint(columnNames={"ObjectId", "FieldId"})
//		})
//public class ObjectEntryProp implements Serializable {
//
//	private static final long serialVersionUID = -5402146930392832758L;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "Id")
//	private long id;
//
//	@ManyToOne(fetch = FetchType.EAGER, optional = false)
//	@JoinColumn(name = "ObjectId", nullable = false, updatable = true)
//	private ObjectEntryEntity object;
//
//	@ManyToOne(fetch = FetchType.EAGER, optional = false)
//	@JoinColumn(name = "FieldId", nullable = false, updatable = true)
//	private ObjectEntryField field;
//
//	@Column(name = "Value", nullable = false)
//	private String value;
//}
