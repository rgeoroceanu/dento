package com.company.dento.model.business;

import com.company.dento.model.type.CalendarEventType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "samples")
public class Sample extends Base {
	
	@ManyToOne(optional = false)
	private SampleTemplate template;
	@ManyToOne(optional = false)
	private Job job;
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	private CalendarEvent dateEvent;
	
	public String toString() {
		if (template != null) {
			return template.getName() + " : " + job.getId();
		}
		return super.toString();
	}

	public void setDate(final LocalDate deliveryDate) {
		if (deliveryDate == null) {
			dateEvent = null;
			return;
		}

		if (this.dateEvent == null) {
			this.dateEvent = new CalendarEvent(CalendarEventType.SAMPLE);
		}
		this.dateEvent.setDate(deliveryDate);
	}

	public void setTime(final LocalTime deliveryTime) {
		if (dateEvent == null) {
			return;
		}
		dateEvent.setTime(deliveryTime);
	}

	public LocalDate getDate() {
		return dateEvent != null ? dateEvent.getDate() : null;
	}

	public LocalTime getTime() {
		return dateEvent != null ? dateEvent.getTime() : null;
	}
}
