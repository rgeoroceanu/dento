package com.company.dento.model.business;

import com.company.dento.model.type.Role;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = true, exclude = {"roles"})
public class User extends Base {
	
	@ElementCollection(fetch = FetchType.EAGER)
	private Set<Role> roles = new HashSet<>();

	@Column(unique = true, nullable = false)
	private String username;

	@Column(nullable = false)
	private String password;

	@Basic
	private String firstName;

	@Basic
	private String lastName;

	@Basic
	private boolean active;
	
	public String toString() {
		if (lastName != null && firstName != null) {
			return firstName + " " + lastName;
		}
		return super.toString();
	}
}
