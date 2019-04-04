package com.company.dento.model.business;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public class Job extends Base {
	
	@ManyToOne(optional = false)
	private JobTemplate template;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Order order;

	@Basic
	private int price;

	@Basic
	private int count = 1;

	@OneToMany
	private List<MaterialTemplate> usedMaterials = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Execution> executions = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Sample> samples = new ArrayList<>();

	@Basic
	private LocalDate deliveryDate;

	@Basic
	private LocalTime deliveryTime;

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<Tooth> teeth = new HashSet<>();
	
	public String toString() {
		if (template != null && order != null) {
			return template.getName() + " : " + order.getId();
		}
		return super.toString();
	}
}
