package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode(doNotUseGetters = true)
public class MaterialPrice implements Price<JobTemplate> {

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private JobTemplate jobTemplate;

	@Column(precision=8, scale=2)
	private float price;

	@Override
	public JobTemplate getKey() {
		return jobTemplate;
	}

	@Override
	public void setKey(final JobTemplate key) {
		this.jobTemplate = key;
	}

	@Override
	public String getKeyName() {
		return jobTemplate.getName();
	}
}
