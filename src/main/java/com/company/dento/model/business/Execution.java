package com.company.dento.model.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
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
	@ManyToOne
	private Procedure procedure;
	@ManyToOne
	private User technician;
	@Basic
	private int duration;
	@Basic
	private int spentTime;
	@Basic
	private int progressPercentage;
	@OneToMany
	private List<MaterialTemplate> usedMaterials = new ArrayList<>();
	@Basic
	private String description;
	@ElementCollection
	private List<String> comments = new ArrayList<>();
	
	
}
