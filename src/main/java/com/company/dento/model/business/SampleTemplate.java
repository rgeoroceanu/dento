package com.company.dento.model.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sample_templates")
public class SampleTemplate extends Base {
	
	@Column(unique = true, nullable = false)
	private String name;
	
}
