package com.company.dento.model.business;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order extends Base {
	
	@ManyToOne
	private Doctor doctor;
	@Basic
	private String patient;
	@ManyToOne
	private Clinic clinic;
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
	@Basic
	private boolean finalized;
	@Basic
	private boolean paid;

}
