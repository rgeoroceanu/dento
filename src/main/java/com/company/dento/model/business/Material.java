package com.company.dento.model.business;

import lombok.*;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "materials")
@EqualsAndHashCode(callSuper = true, exclude = {"job"})
@NoArgsConstructor
@AllArgsConstructor
public class Material extends Base {

	@ManyToOne(optional = false)
	private MaterialTemplate template;

	@ManyToOne(optional = false)
	private Job job;

	@Basic
	private float price;

	@Basic
	private float quantity;


}
