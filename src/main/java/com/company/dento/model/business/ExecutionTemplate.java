package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
@Table(name = "execution_templates")
@EqualsAndHashCode(callSuper = true, exclude = "individualPrices")
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
