package com.company.dento.model.business;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "patients")
public class Patient extends Base {

	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String address;
	
}
