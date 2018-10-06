package com.company.dento.service;

import java.util.List;
import java.util.Map;

import com.company.dento.model.business.*;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.service.exception.InvalidDataTypeException;
import org.springframework.data.jpa.domain.Specification;

public interface DataService {
	
	<T extends Base> T saveEntity(final T entity) throws InvalidDataTypeException;
	<T extends Base> List<T> getAll(final Class<T> entityClass) throws InvalidDataTypeException;
	<T extends Base> T getEntity(final Long entityId, final Class<T> entityClass) throws InvalidDataTypeException, DataDoesNotExistException;
	<T extends Base> void deleteEntity(final Long entityId, final Class<T> entityClass) throws InvalidDataTypeException, DataDoesNotExistException;
	List<Sample> getProcedureSamples(final Long procedureId);
	List<Job> getProcedureExecutions(final Long procedureId);
	User getUser(final String username) throws DataDoesNotExistException;
	User saveUserAndEncodePassword(final User user);
	<T extends Base> List<T> getByCriteria(final Class<T> itemClass,
										final Specification<T> criteria,
										final int offset, final int limit,
										final Map<String, Boolean> sortOrder);
	<T extends Base> int countByCriteria(final Class<T> itemClass, final Specification<T> criteria);
}
