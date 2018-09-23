package com.company.dento.model.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.company.dento.model.type.SelectionType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "job_templates")
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
	
	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}
}
