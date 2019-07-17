package com.company.dento.model.business;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Getter
@Setter
@Entity
@Table(name = "doctors")
public class Doctor extends Base implements SoftDelete {
	
	@Basic
	private String firstName;

	@Basic
	private String lastName;

	@ManyToOne
	private Clinic clinic;

	@Basic
	private String email;

	@Basic
	private String phone;

	@Basic
	private boolean deleted;

	@Basic
	private boolean active = true;

	@Override
	public String toString() {
		if (lastName != null && firstName != null) {
			return firstName + " " + lastName;
		}
		return super.toString();
	}
}
