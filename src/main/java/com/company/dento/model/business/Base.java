package com.company.dento.model.business;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Base entity with an id, version, created and updated fiels.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Getter
@Setter
@MappedSuperclass
@EqualsAndHashCode
public class Base {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Version
	private int version;
	@Column
	private LocalDateTime created;
	@Column
	private LocalDateTime updated;
	
	@PreUpdate
	@PrePersist
	public void updateTimeStamps() {
	    updated = LocalDateTime.now();
	    if (null == created) {
	    	created = LocalDateTime.now();
	    }
	}
}
