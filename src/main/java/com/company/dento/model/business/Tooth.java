package com.company.dento.model.business;

import com.company.dento.model.type.ToothProperty;
import com.company.dento.model.type.ToothType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class Tooth {

	private int number;

	private ToothType type;

	private ToothProperty property;

	public Tooth() {}

	public Tooth(final int number) {
		this.number = number;
	}
	
}
