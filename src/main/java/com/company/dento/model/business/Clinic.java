package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "clinics")
@EqualsAndHashCode(callSuper = true)
public class Clinic extends Base {

	@Column(unique = true, nullable = false)
	private String name;

	@Column(nullable = false)
	private String email;

	@Basic
	private String phone;

	@Basic
	private String town;

	@Basic
	private String address;
	
	@Override
	public String toString() {
		if (name != null) {
			return name;
		}
		return super.toString();
	}
}
