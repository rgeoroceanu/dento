package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
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
public class ExecutionTemplate extends Base implements SoftDelete {
	
	@Column(unique = true, nullable = false)
	private String name;

	@Column(precision=8, scale=2)
	private float standardPrice;

	@Basic
	private boolean active = true;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<ExecutionPrice> individualPrices  = new LinkedHashSet<>();

	@Basic
	private int coefficient;

	@Basic
	private boolean deleted;
}
