package com.company.dento.model.business;

import com.company.dento.model.type.SelectionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "job_templates")
@EqualsAndHashCode(callSuper = true, exclude = {"individualPrices", "materials", "sampleTemplates", "executionTemplates"})
public class JobTemplate extends Base {
	
	@Column(unique = true, nullable = false)
	private String name;

	@Column
	@Enumerated
	private SelectionType selectionType;

	@Basic
	private String color;

	@Basic
	private int standardPrice;

	@Basic
	private boolean active;

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<JobPrice> individualPrices = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Material> materials = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<SampleTemplate> sampleTemplates = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<ExecutionTemplate> executionTemplates = new HashSet<>();

	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}
}
