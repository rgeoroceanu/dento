package com.company.dento.model.business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "procedures")
public class Procedure extends Base {
	
	@OneToOne
	private Doctor doctor;
	@OneToOne
	private Patient patient;
	@ManyToOne(optional = false)
	private ProcedureTemplate template;
	@Column
	private String description;
	@ElementCollection
	private List<Tooth> teeth = new ArrayList<>();
	@Column
	private LocalDateTime deliveryDate;
	@ElementCollection
	private List<Sample> samples = new ArrayList<>();
	@ElementCollection
	private List<Execution> executions = new ArrayList<>();
	@ElementCollection
	private List<String> cadFiles = new ArrayList<>();
}
