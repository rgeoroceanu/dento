package com.company.dento.model.business;

import com.company.dento.model.type.MeasurementUnit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "material_templates")
@EqualsAndHashCode(callSuper = true, exclude = "individualPrices")
public class Material extends Base {
	
	@Column(unique = true, nullable = false)
	private String name;
	@Enumerated
	private MeasurementUnit measurementUnit;
	@Basic
	private int pricePerUnit;
	@Basic
	private boolean perJob;
	@OneToMany
	private List<MaterialPrice> individualPrices = new ArrayList<>();
	@Basic
	private boolean active;
	
	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}
}
