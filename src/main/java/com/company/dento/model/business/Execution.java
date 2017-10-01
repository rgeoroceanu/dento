package com.company.dento.model.business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Getter
@Setter
@Entity
@Table(name = "executions")
public class Execution extends Base {
	
	@ManyToOne(optional = false)
	private ExecutionTemplate template;
	@ManyToOne(fetch = FetchType.EAGER)
	private Procedure procedure;
	@ManyToOne
	private User technician;
	@Basic
	private int estimatedDuration;
	@Basic
	private int price;
	@Column
	private LocalDateTime deadlineDate;
	@Enumerated
	private Priority priority;
	@Enumerated
	private Status status;
	@Basic
	private int spentTime;
	@Basic
	private int progressPercentage;
	@OneToMany
	private List<MaterialTemplate> usedMaterials = new ArrayList<>();
	@Basic
	private String description;
	//@ElementCollection
	//private List<String> comments = new ArrayList<>();
	
	
}
