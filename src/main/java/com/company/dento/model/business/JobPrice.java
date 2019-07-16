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
public class JobPrice implements Price<Clinic> {
	
	@OneToOne
	private Clinic clinic;

	@Basic
	private float price;

	@Override
	public Clinic getKey() {
		return clinic;
	}

	@Override
	public void setKey(final Clinic key) {
		this.clinic = key;
	}

	@Override
	public String getKeyName() {
		return clinic.getName();
	}
}
