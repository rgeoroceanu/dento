package com.company.dento.model.business;

import com.company.dento.model.type.Currency;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "general_data")
@EqualsAndHashCode(callSuper = true)
public class GeneralData extends Base {

	@Column(nullable = false)
	private String laboratoryName;

	@Column
	private String email;

	@Basic
	private String phone;

	@Basic
	private String town;

	@Basic
	private String address;

	@Basic
	private String postalCode;

	@Embedded
	private StoredFile logo;

	@Enumerated(EnumType.STRING)
	private Currency currency = Currency.RON;

	@Lob
	private String reportEmailText;
}
