package com.company.dento.model.business;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@Table(name = "execution_prices")
public class ExecutionPrice extends Base implements Price<User> {
	
	@OneToOne
	private User technician;

	@Basic
	private int price;

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
