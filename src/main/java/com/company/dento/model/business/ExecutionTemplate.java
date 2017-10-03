package com.company.dento.model.business;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "execution_templates")
public class ExecutionTemplate extends Base {
	
	@Column(unique = true, nullable = false)
	private String name;
	@Basic
	private int estimatedDuration;
	@Basic
	private int price;
	@Basic
	private Priority priority;
	
	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}
}
