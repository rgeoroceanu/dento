package com.company.dento.model.business;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DefaultMaterial {

	@ManyToOne(optional = false)
	private MaterialTemplate template;

	@Basic
	private float quantity;
}
