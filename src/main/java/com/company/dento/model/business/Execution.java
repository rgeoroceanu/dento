package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Getter
@Setter
@Entity
@Table(name = "executions")
@EqualsAndHashCode(callSuper = true, exclude = {"job", "technician"})
public class Execution extends Base {
	
	@ManyToOne(optional = false)
	private ExecutionTemplate template;

	@ManyToOne(optional = false)
	private Job job;

	@Column(precision=8, scale=2)
	private float price;

	@Basic
	private int count = 1;

	@ManyToOne
	private User technician;
}
