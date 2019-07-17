package com.company.dento.model.business;

import com.company.dento.model.type.MeasurementUnit;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "material_templates")
@EqualsAndHashCode(callSuper = true, exclude = "individualPrices")
public class MaterialTemplate extends Base implements SoftDelete {
	
	@Column(unique = true, nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
	private MeasurementUnit measurementUnit;

	@Basic
	private float pricePerUnit;

	@Basic
	private boolean perJob;

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<MaterialPrice> individualPrices = new HashSet<>();

	@Basic
	private boolean active = true;

	@Basic
	private boolean deleted;

	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}
}
