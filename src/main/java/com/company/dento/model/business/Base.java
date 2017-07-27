package com.company.dento.model.business;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Version;

import lombok.Getter;
import lombok.Setter;

/**
 * Base entity with an id, version, created and updated fiels.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Getter
@Setter
@MappedSuperclass
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
