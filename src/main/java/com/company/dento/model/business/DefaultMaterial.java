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

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private MaterialTemplate template;

	@Column(precision=8, scale=2)
	private float quantity;
}
