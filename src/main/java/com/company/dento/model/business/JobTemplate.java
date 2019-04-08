package com.company.dento.model.business;

import com.company.dento.model.type.SelectionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "job_templates")
@EqualsAndHashCode
public class JobTemplate extends Base {
	
	@Column(unique = true, nullable = false)
	private String name;

	@Enumerated
	private SelectionType selectionType;

	@Basic
	private String color;

	@Basic
	private int standardPrice;

	@Basic
	private boolean active;

	@OneToMany
	private List<JobPrice> individualPrices = new ArrayList<>();

	@OneToMany
	private List<MaterialTemplate> defaultMaterials = new ArrayList<>();

	@ManyToMany
	private List<SampleTemplate> sampleTemplates = new ArrayList<>();

	@ManyToMany
	private List<ExecutionTemplate> executionTemplates = new ArrayList<>();

	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}
}
