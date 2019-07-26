package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode(doNotUseGetters = true)
public class JobPrice implements Price<Clinic> {
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Clinic clinic;

	@Column(precision=8, scale=2)
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
