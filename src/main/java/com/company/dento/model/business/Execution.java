package com.company.dento.model.business;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
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
@Table(name = "executions")
public class Execution extends Base {
	
	@ManyToOne(optional = false)
	private ExecutionTemplate template;
	@ManyToOne(fetch = FetchType.EAGER)
	private Job job;
	@Basic
	private int price;
	@Basic
	private int count = 1;
	@ManyToOne
	private User technician;
}
