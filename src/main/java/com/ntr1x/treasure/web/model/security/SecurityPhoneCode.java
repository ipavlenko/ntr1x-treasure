//package com.ntr1x.treasure.web.model.security;
//
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Table;
//import javax.persistence.UniqueConstraint;
//
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//@Table(
//		name = "security_phone_codes",
//		uniqueConstraints = {
//				@UniqueConstraint(columnNames = {"Phone" })
//		}
//)
//@NamedQueries({
//		@NamedQuery(
//				name = "SecurityPhoneCode.accessibleOfPhone",
//				query =
//						"	SELECT c"
//								+ "	FROM SecurityPhoneCode c"
//								+ "	WHERE "
//								+ "	  c.phone = :phone"
//		),
//		@NamedQuery(
//				name = "SecurityPhoneCode.accessibleOfPhoneAndCode",
//				query =
//						"	SELECT c"
//						+ "	FROM SecurityPhoneCode c"
//						+ "	WHERE "
//						+ "	  c.phone = :phone AND c.code = :code"
//		)
//})
//public class SecurityPhoneCode {
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
//	@Column(name = "Id")
//	private Long id;
//
//	@Column(name = "Phone", nullable = false)
//	private long phone;
//
//	@Column(name = "Code", nullable = false)
//	private int code;
//}
