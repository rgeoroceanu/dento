package com.company.dento.model.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

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
	@ManyToOne(fetch = FetchType.EAGER)
	private Order order;
	@ManyToOne
	private User technician;
	@Basic
	private int price;
	@OneToMany
	private List<MaterialTemplate> usedMaterials = new ArrayList<>();
	
	public String toString() {
		if (template != null && order != null) {
			return template.getName() + " : " + order.getId();
		}
		return super.toString();
	}
}
