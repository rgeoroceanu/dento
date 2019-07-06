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
@Table(name = "calendar_events")
public class CalendarEvent extends Base {

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CalendarEventType type;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private LocalTime time;

    @Column
    private String text;

    public CalendarEvent() { }

    public CalendarEvent(final CalendarEventType type) {
        this.type = type;
    }
}
