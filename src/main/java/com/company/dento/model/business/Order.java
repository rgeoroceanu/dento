package com.company.dento.model.business;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
	@Basic
	@Column(length = 4000)
	private String description;
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Tooth> teeth = new HashSet<>();
	@Column
	private LocalDate date;
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Sample> samples = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "order")
	private List<Job> jobs = new ArrayList<>();
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	private List<CadFile> cadFiles = new ArrayList<>();
	@Basic
	private boolean finalized;
	@Basic
	private boolean paid;
	@ManyToOne
	private Color color;
	@Basic
	private int partialSum;

}
