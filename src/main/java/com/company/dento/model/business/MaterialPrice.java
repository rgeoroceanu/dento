package com.company.dento.model.business;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "material_prices")
public class MaterialPrice extends Base {
	
	@OneToOne
	private JobTemplate jobTemplate;
	@Basic
	private int price;
}
