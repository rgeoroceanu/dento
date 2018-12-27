package com.company.dento.model.business;

import javax.persistence.Embeddable;

import com.company.dento.model.type.ToothProperty;
import com.company.dento.model.type.ToothType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
public class Tooth {
	private int number;
	private ToothType type;
	private ToothProperty property;
	
}
