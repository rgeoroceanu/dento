package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

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

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<ExecutionPrice> individualPrices  = new HashSet<>();

	@Basic
	private int coefficient;
}
