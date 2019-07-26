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
public class ExecutionPrice implements Price<User> {

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private User technician;

	@Column(precision=8, scale=2)
	private float price;

	@Override
	public User getKey() {
		return technician;
	}

	@Override
	public void setKey(final User key) {
		this.technician = key;
	}

	@Override
	public String getKeyName() {
		return String.format("%s %s", technician.getFirstName(), technician.getLastName());
	}
}
