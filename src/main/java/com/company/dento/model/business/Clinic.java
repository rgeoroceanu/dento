package com.company.dento.model.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "clinics")
public class Clinic extends Base {

	@Column(unique = true, nullable = false)
	private String name;
	@OneToMany
	private List<Doctor> doctors = new ArrayList<>();
	@Basic
	private String email;
	@Basic
	private String phone;
	
}
