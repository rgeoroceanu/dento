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
@Table(name = "job_prices")
public class JobPrice extends Base {
	
	@OneToOne
	private Clinic clinic;
	@Basic
	private int price;
}
