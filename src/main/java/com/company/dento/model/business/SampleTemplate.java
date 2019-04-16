package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "sample_templates")
@EqualsAndHashCode(callSuper = true)
public class SampleTemplate extends Base {
	
	@Column(unique = true, nullable = false)
	private String name;

	@Basic
	private boolean active;
	
	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}
}
