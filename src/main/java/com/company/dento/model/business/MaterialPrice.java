package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Basic;
import javax.persistence.Embeddable;
import javax.persistence.OneToOne;

@Getter
@Setter
@ToString
@Embeddable
@EqualsAndHashCode
public class MaterialPrice implements Price<JobTemplate> {
	
	@OneToOne
	private JobTemplate jobTemplate;

	@Basic
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
