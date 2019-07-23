package com.company.dento.model.business;

import com.company.dento.model.type.SelectionType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "job_templates")
@EqualsAndHashCode(callSuper = true, exclude = {"individualPrices", "materials", "sampleTemplates", "executionTemplates"})
public class JobTemplate extends Base implements SoftDelete {
	
	@Column(unique = true, nullable = false)
	private String name;

	@Column
	@Enumerated(EnumType.STRING)
	private SelectionType selectionType;

	@Basic
	private String color;

	@Basic
	private float standardPrice;

	@Basic
	private boolean active = true;

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<JobPrice> individualPrices = new HashSet<>();

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<DefaultMaterial> materials = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="job_templates_samples", joinColumns=@JoinColumn(name="job_template_id"), inverseJoinColumns=@JoinColumn(name="sample_template_id"))
	@OrderBy(value = "id")
	private Set<SampleTemplate> sampleTemplates = new LinkedHashSet<>();

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name="job_templates_executions", joinColumns=@JoinColumn(name="job_template_id"), inverseJoinColumns=@JoinColumn(name="execution_template_id"))
	@OrderBy(value = "id")
	private Set<ExecutionTemplate> executionTemplates = new LinkedHashSet<>();

	@Basic
	private boolean deleted;

	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}
}
