package com.company.dento.model.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "procedure_templates")
public class ProcedureTemplate extends Base {
	
	@Column(unique = true, nullable = false)
	private String name;
	@Basic
	private int price;
	@OneToMany
	private List<SampleTemplate> samples = new ArrayList<>();
	@OneToMany
	private List<ExecutionTemplate> executions = new ArrayList<>();
	
	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}
}
