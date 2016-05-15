package com.ntr1x.treasure.model.catalog;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ObjectEntryParamLW implements Serializable {

	private static final long serialVersionUID = -5540356665099681181L;

	@Column(name = "Name", nullable = false)
	private String name;
	
	@Column(name = "Value", nullable = false)
	private String value;
}
