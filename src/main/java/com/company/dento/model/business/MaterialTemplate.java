package com.company.dento.model.business;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.company.dento.model.type.MeasurementUnit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "material_templates")
public class MaterialTemplate extends Base {
	
	@Column(unique = true, nullable = false)
	private String name;
	@Enumerated
	private MeasurementUnit measurementUnit;
	@Basic
	private int pricePerUnit;
	@Basic
	private boolean perProcedure;
	
	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}
}
