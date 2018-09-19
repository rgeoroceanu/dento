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

/**
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Getter
@Setter
@Entity
@Table(name = "execution_templates")
public class ExecutionTemplate extends Base {
	
	@Column(unique = true, nullable = false)
	private String name;
	@Basic
	private int standardPrice;
	@Basic
	private boolean active;
	@OneToMany
	private List<ExecutionPrice> individualPrices  = new ArrayList<>();
	@Basic
	private int coefficient;
}
