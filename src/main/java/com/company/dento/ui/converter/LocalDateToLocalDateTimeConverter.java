package com.company.dento.ui.converter;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 *
 *
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public class LocalDateToLocalDateTimeConverter implements Converter<LocalDate, LocalDateTime> {

	private static final long serialVersionUID = 1L;

	@Override
	public Result<LocalDateTime> convertToModel(LocalDate value, ValueContext context) {
		if (value == null) {
			return null;
		}
		return Result.ok(value.atStartOfDay());
	}

	@Override
	public LocalDate convertToPresentation(LocalDateTime value, ValueContext context) {
		if (value == null) {
			return null;
		}
		return value.toLocalDate();
	}

}
