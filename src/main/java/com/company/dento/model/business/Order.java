package com.company.dento.model.business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends Base {
	
	@OneToOne
	private Doctor doctor;
	@Basic
	private String patient;
	@ManyToOne(optional = false)
	private OrderTemplate template;
	@Basic
	private int price;
	@Column
	private String description;
	@ElementCollection
	private List<Tooth> teeth = new ArrayList<>();
	@Column
	private LocalDateTime deliveryDate;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Sample> samples = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Job> jobs = new ArrayList<>();
	@ElementCollection
	private List<String> cadFiles = new ArrayList<>();
	
	public String toString() {
		if (template != null) {
			return template.getName() + " : Id " + super.getId();
		}
		return super.toString();
	}
}
