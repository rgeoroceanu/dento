package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
public class JobPrice {
	
	@OneToOne
	private Clinic clinic;

	@Basic
	private int price;
}
