package com.ntr1x.treasure.web.model.security;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
		name = "security_phone_codes",
		uniqueConstraints = {
				@UniqueConstraint(columnNames = {"Phone" })
		}
)
@NamedQueries({
		@NamedQuery(
				name = "SecurityPhoneCode.accessibleOfPhone",
				query =
						"	SELECT c"
								+ "	FROM SecurityPhoneCode c"
								+ "	WHERE "
								+ "	  c.phone = :phone"
		),
		@NamedQuery(
				name = "SecurityPhoneCode.accessibleOfPhoneAndCode",
				query =
						"	SELECT c"
						+ "	FROM SecurityPhoneCode c"
						+ "	WHERE "
						+ "	  c.phone = :phone AND c.code = :code"
		)
})
public class SecurityPhoneCode implements Serializable {

	private static final long serialVersionUID = 3223740485148213015L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id")
	private long id;

	@Column(name = "Phone", nullable = false)
	private long phone;

	@Column(name = "Code", nullable = false)
	private int code;
}
