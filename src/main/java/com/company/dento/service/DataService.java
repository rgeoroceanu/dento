package com.company.dento.service;

import com.company.dento.dao.specification.MaterialSpecification;
import com.company.dento.dao.specification.OrderSpecification;
import com.company.dento.model.business.*;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.service.exception.InvalidDataTypeException;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface DataService {
	
	<T extends Base> T saveEntity(final T entity) throws InvalidDataTypeException;
	<T extends Base> List<T> getAll(final Class<T> entityClass) throws InvalidDataTypeException;
	<T extends Base> Optional<T> getEntity(final Long entityId, final Class<T> entityClass) throws InvalidDataTypeException;
	<T extends Base> void deleteEntity(final Long entityId, final Class<T> entityClass) throws InvalidDataTypeException;
	<T extends Base & SoftDelete> void softDeleteEntity(final Long entityId, final Class<T> entityClass) throws InvalidDataTypeException;
	User getUser(final String username) throws DataDoesNotExistException;
	User saveUserAndEncodePassword(final User user);
	<T extends Base> List<T> getByCriteria(final Class<T> itemClass,
										final Specification<T> criteria,
										final int offset, final int limit,
										final Map<String, Boolean> sortOrder);
	<T extends Base> int countByCriteria(final Class<T> itemClass, final Specification<T> criteria);
	List<CalendarEvent> getCalendarEvents(final LocalDate start, final LocalDate end);
	Optional<GeneralData> getGeneralData();
	GeneralData saveGeneralData(final GeneralData generalData);
	List<User> getAllTechnicians();
	double getExecutionsPriceTotal(final OrderSpecification spec);
	double getJobsPriceTotal(final OrderSpecification spec);
	double getMaterialPriceTotal(final MaterialSpecification spec);
	Order saveOrder(final Order order);
}
