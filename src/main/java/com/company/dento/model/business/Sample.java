package com.company.dento.model.business;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "samples")
public class Sample extends Base {
	
	@ManyToOne(optional = false)
	private SampleTemplate template;
	@ManyToOne(optional = false)
	private Job job;
	@Column
	private LocalDate date;
	@Column
	private LocalTime time;
	
	public String toString() {
		if (template != null) {
			return template.getName() + " : " + job.getId();
		}
		return super.toString();
	}
}
