package com.company.dento.model.business;

import com.company.dento.model.type.CalendarEventType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
@Getter
@Setter
@Entity
@Table(name = "jobs")
public class Job extends Base {
	
	@ManyToOne(optional = false)
	private JobTemplate template;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Order order;

	@Basic
	private int price;

	@Basic
	private int count = 1;

	@OneToMany
	private List<MaterialTemplate> usedMaterials = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Execution> executions = new ArrayList<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "job", orphanRemoval = true, cascade = CascadeType.ALL)
	private List<Sample> samples = new ArrayList<>();

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private CalendarEvent deliveryEvent;

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<Tooth> teeth = new HashSet<>();
	
	public String toString() {
		if (template != null && order != null) {
			return template.getName() + " : " + order.getId();
		}
		return super.toString();
	}

	public void setDeliveryDate(final LocalDate deliveryDate) {
		if (deliveryDate == null) {
			deliveryEvent = null;
			return;
		}

		if (this.deliveryEvent == null) {
			this.deliveryEvent = new CalendarEvent(CalendarEventType.JOB_DELIVERY);
		}
		this.deliveryEvent.setDate(deliveryDate);
	}

	public void setDeliveryTime(final LocalTime deliveryTime) {
		if (deliveryEvent == null) {
			return;
		}
		deliveryEvent.setTime(deliveryTime);
	}

	public LocalDate getDeliveryDate() {
		return deliveryEvent != null ? deliveryEvent.getDate() : null;
	}

	public LocalTime getDeliveryTime() {
		return deliveryEvent != null ? deliveryEvent.getTime() : null;
	}
}
