package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
@EqualsAndHashCode(callSuper = true, exclude = {"jobs", "storedFiles"})
public class Order extends Base {
	
	@ManyToOne
	private Doctor doctor;

	@Basic
	private String patient;

	@ManyToOne
	private Clinic clinic;

	@Basic
	@Column(length = 4000)
	private String description;

	@Column
	private LocalDate date;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "order")
	private Set<Job> jobs = new HashSet<>();

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<StoredFile> storedFiles = new HashSet<>();

	@Basic
	private boolean finalized;

	@Basic
	private boolean paid;

	@ManyToOne
	private ToothColor toothColor;

	@Basic
	private int partialSum;

	@Basic
	private LocalDateTime deliveryDate;

	public Double getTotalPrice() {
		return this.getJobs().stream().mapToDouble(job -> job.getPrice() * job.getCount()).sum();
	}

}
