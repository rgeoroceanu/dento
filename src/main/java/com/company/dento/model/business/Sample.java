package com.company.dento.model.business;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "samples")
public class Sample extends Base {
	
	@ManyToOne(optional = false)
	private SampleTemplate template;
	@ManyToOne(fetch = FetchType.EAGER)
	private Procedure procedure;
	@ElementCollection
	private List<String> images = new ArrayList<>();
	
}
