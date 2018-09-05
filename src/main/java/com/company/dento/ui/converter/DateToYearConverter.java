package com.company.dento.ui.converter;

import java.util.Date;

import org.joda.time.DateTime;

import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

/**
 * Vaadin data {@link Converter} that converts Date to the year representation.
 * 
 * @author Radu Georoceanu <rgeoroceanu@yahoo.com>
 *
 */
public class DateToYearConverter implements Converter<Date, Integer> {

	private static final long serialVersionUID = 1L;

	@Override
	public Result<Integer> convertToModel(Date value, ValueContext context) {
		if (value == null) {
			return null;
		}
		return Result.ok(new DateTime(value).getYear());
	}

	@Override
	public Date convertToPresentation(Integer value, ValueContext context) {
		if (value == null || value == 0) {
			return null;
		}
		return new DateTime(value, 1, 1, 1, 1).toDate();
	}

}
