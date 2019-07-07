package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class Tooth {

	private int number;

	@ManyToOne(fetch = FetchType.EAGER)
	private ToothOption option1;

	@ManyToOne(fetch = FetchType.EAGER)
	private ToothOption option2;

	public Tooth() {}

	public Tooth(final int number) {
		this.number = number;
	}
	
}
