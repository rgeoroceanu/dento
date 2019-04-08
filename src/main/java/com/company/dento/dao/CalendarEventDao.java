package com.company.dento.dao;

import com.company.dento.model.business.CalendarEvent;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CalendarEventDao extends PageableRepository<CalendarEvent, Long> {
	List<CalendarEvent> findByDateBetween(final LocalDate start, final LocalDate end);
}
