package com.company.dento.model.business;

import com.company.dento.model.type.CalendarEventType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "orders")
@EqualsAndHashCode(callSuper = true, exclude = {"jobs", "storedFiles"})
public class Order extends Base {
	
	@ManyToOne
	private Doctor doctor;

	@Basic
	private String patient;

	@Basic
	@Column(length = 4000)
	private String description;

	@Column
	private LocalDate date;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "order")
    @OrderBy(value = "id")
	private Set<Job> jobs = new LinkedHashSet<>();

	@ElementCollection(fetch = FetchType.LAZY)
	private Set<StoredFile> storedFiles = new LinkedHashSet<>();

	@Basic
	private boolean finalized;

	@Basic
	private boolean paid;

	@ManyToOne
	private ToothColor toothColor;

	@Basic
	private int partialSum;

	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private CalendarEvent deliveryEvent;

	public Double getTotalPrice() {
		return this.getJobs().stream().mapToDouble(job -> job.getPrice() * job.getCount()).sum();
	}

	public void setDeliveryDate(final LocalDate deliveryDate) {
		if (deliveryDate == null) {
			deliveryEvent = null;
			return;
		}

		if (this.deliveryEvent == null) {
			this.deliveryEvent = new CalendarEvent(CalendarEventType.ORDER_DELIVERY);
			this.deliveryEvent.setText(String.format("Dr. %s (%s)", doctor.getLastName(), doctor.getClinic().getName()));
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
