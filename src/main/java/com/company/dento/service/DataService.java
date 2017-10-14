package com.company.dento.service;

import java.util.List;

import com.company.dento.model.business.Base;
import com.company.dento.model.business.Execution;
import com.company.dento.model.business.Sample;
import com.company.dento.model.business.User;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.service.exception.InvalidDataTypeException;

public interface DataService {
	
	public <T extends Base> T saveEntity(T entity) throws InvalidDataTypeException;
	public <T extends Base> List<T> getAll(Class<T> entityClass) throws InvalidDataTypeException;
	public <T extends Base> T getEntity(Long entityId, Class<T> entityClass) throws InvalidDataTypeException, DataDoesNotExistException;
	public <T extends Base> void deleteEntity(Long entityId, Class<T> entityClass) throws InvalidDataTypeException, DataDoesNotExistException;
	public List<Sample> getProcedureSamples(Long procedureId);
	public List<Execution> getProcedureExecutions(Long procedureId);
	public User getUser(String username) throws DataDoesNotExistException;
	public User saveUserAndEncodePassword(User user);
}
