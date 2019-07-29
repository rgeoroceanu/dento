package com.company.dento.model.business;

import com.company.dento.model.type.CalendarEventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Getter
@Setter
@Entity
@Table(name = "jobs")
@EqualsAndHashCode(callSuper = true, exclude = {"order", "executions", "samples", "teeth", "materials"}, doNotUseGetters = true)
public class Job extends Base {
	
	@ManyToOne(optional = false)
	private JobTemplate template;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Order order;

	@Column(precision=8, scale=2)
	private float price;

	@Basic
	private int count = 1;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job", orphanRemoval = true, cascade = CascadeType.ALL)
	@OrderBy(value = "id")
	private Set<Execution> executions = new LinkedHashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job", orphanRemoval = true, cascade = CascadeType.ALL)
	@OrderBy(value = "id")
	private Set<Sample> samples = new LinkedHashSet<>();

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<Tooth> teeth = new LinkedHashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job", orphanRemoval = true, cascade = CascadeType.ALL)
	@OrderBy(value = "id")
	private Set<Material> materials = new LinkedHashSet<>();

	public String toString() {
		if (template != null && order != null) {
			return template.getName() + " : " + order.getId();
		}
		return super.toString();
	}
}
