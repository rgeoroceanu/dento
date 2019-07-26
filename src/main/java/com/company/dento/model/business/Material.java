package com.company.dento.model.business;

import lombok.*;

import javax.persistence.*;

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

	@Column(precision=8, scale=2)
	private float price;

	@Column(precision=8, scale=2)
	private float quantity;


}
