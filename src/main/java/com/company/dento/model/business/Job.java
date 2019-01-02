package com.company.dento.model.business;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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
	
	public String toString() {
		if (template != null && order != null) {
			return template.getName() + " : " + order.getId();
		}
		return super.toString();
	}
}
