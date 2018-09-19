package com.company.dento.model.business;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "execution_prices")
public class ExecutionPrice extends Base {
	
	@OneToOne
	private User technician;
	@Basic
	private int price;
}
