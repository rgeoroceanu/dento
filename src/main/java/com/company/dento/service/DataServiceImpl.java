package com.company.dento.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.company.dento.dao.ClinicDao;
import com.company.dento.dao.DoctorDao;
import com.company.dento.dao.ExecutionDao;
import com.company.dento.dao.ExecutionTemplateDao;
import com.company.dento.dao.MaterialTemplateDao;
import com.company.dento.dao.ProcedureDao;
import com.company.dento.dao.ProcedureTemplateDao;
import com.company.dento.dao.SampleDao;
import com.company.dento.dao.SampleTemplateDao;
import com.company.dento.dao.UserDao;
import com.company.dento.model.business.Base;
import com.company.dento.model.business.Clinic;
import com.company.dento.model.business.Doctor;
import com.company.dento.model.business.Execution;
import com.company.dento.model.business.ExecutionTemplate;
import com.company.dento.model.business.MaterialTemplate;
import com.company.dento.model.business.Procedure;
import com.company.dento.model.business.ProcedureTemplate;
import com.company.dento.model.business.Sample;
import com.company.dento.model.business.SampleTemplate;
import com.company.dento.model.business.User;
import com.company.dento.model.type.MeasurementUnit;
import com.company.dento.model.type.Role;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.service.exception.InvalidDataTypeException;
import com.google.common.base.Preconditions;

@Service
public class DataServiceImpl implements DataService {
	
	private static final Logger LOG = Logger.getLogger(DataServiceImpl.class);
	@Autowired
	private ExecutionDao executionDao;
	@Autowired
	private ExecutionTemplateDao executionTemplateDao;
	@Autowired
	private MaterialTemplateDao materialTemplateDao;
	@Autowired
	private ProcedureTemplateDao procedureTemplateDao;
	@Autowired
	private ProcedureDao procedureDao;
	@Autowired
	private SampleDao sampleDao;
	@Autowired
	private SampleTemplateDao sampleTemplateDao;
	@Autowired
	private DoctorDao doctorDao;
	@Autowired
	private UserDao userDao;
	@Autowired
	private ClinicDao clinicDao;
	
	@PostConstruct
	public void init() throws InvalidDataTypeException {
		if (userDao.findAll().isEmpty()) {
			User user = new User();
			user.setFirstName("Test");
			user.setLastName("User");
			user.setUsername("user");
			user.setPassword("user");
			user.setRoles(new HashSet<>(Arrays.asList(Role.USER)));
			saveEntity(user);
			
			ExecutionTemplate executionTemplate1 = new ExecutionTemplate();
			executionTemplate1.setDuration(1);
			executionTemplate1.setName("Executie Ceramica");
			ExecutionTemplate executionTemplate2 = new ExecutionTemplate();
			executionTemplate2.setDuration(2);
			executionTemplate2.setName("Executie CAD");
			saveEntity(executionTemplate1);
			saveEntity(executionTemplate2);
			
			SampleTemplate sampleTemplate1 = new SampleTemplate();
			sampleTemplate1.setName("Proba Ceramica");
			saveEntity(sampleTemplate1);
			
			ProcedureTemplate procedureTemplate = new ProcedureTemplate();
			procedureTemplate.setName("Ceramica Zr");
			procedureTemplate.setPrice(100);
			procedureTemplate.getExecutions().add(executionTemplate1);
			procedureTemplate.getExecutions().add(executionTemplate2);
			procedureTemplate.getSamples().add(sampleTemplate1);
			saveEntity(procedureTemplate);
			
			MaterialTemplate material = new MaterialTemplate();
			material.setName("Material 1");
			material.setMeasurementUnit(MeasurementUnit.CM);
			material.setPerProcedure(true);
			material.setPricePerUnit(100);
			saveEntity(material);
			
			Clinic clinic = new Clinic();
			clinic.setName("Clinic 1");
			clinic.setEmail("clinic@mail.com");
			saveEntity(clinic);
			
			Doctor doctor = new Doctor();
			doctor.setFirstName("Dr.");
			doctor.setLastName("Doctor");
			doctor.setEmail("test@test");
			doctor.setClinic(clinic);
			saveEntity(doctor);
			
			Procedure procedure = new Procedure();
			procedure.setTemplate(procedureTemplate);
			procedure.setDeliveryDate(LocalDateTime.now());
			procedure.setDoctor(doctor);
			procedure.setPrice(111);
			procedure.setPatient("Gheorghe");
			saveEntity(procedure);
			
			Execution execution = new Execution();
			execution.setTechnician(user);
			execution.setTemplate(executionTemplate1);
			execution.setProcedure(procedure);
			
			Sample sample = new Sample();
			sample.setTemplate(sampleTemplate1);
			sample.setProcedure(procedure);
			
			procedure.getSamples().add(sample);
			procedure.getExecutions().add(execution);
			saveEntity(procedure);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public <T extends Base> T saveEntity(T entity) throws InvalidDataTypeException {
		Preconditions.checkNotNull(entity, "Entity cannot be null!");
		LOG.info("Saving entity of type " + entity.getClass().getSimpleName() + ": " + entity);
		
		JpaRepository<T, Long> dao = (JpaRepository<T, Long>) getDaoByEntityClass(entity.getClass());
		
		return dao.saveAndFlush(entity);
	}

	@Override
	@Transactional
	public <T extends Base> List<T> getAll(Class<T> entityClass) throws InvalidDataTypeException {
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		LOG.info("Retrieving all entitities of type " + entityClass.getSimpleName());
		
		JpaRepository<T, Long> dao = (JpaRepository<T, Long>) getDaoByEntityClass(entityClass);
		
		return dao.findAll();
	}

	@Override
	@Transactional
	public <T extends Base> T getEntity(Long entityId, Class<T> entityClass) throws InvalidDataTypeException, DataDoesNotExistException {
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		Preconditions.checkNotNull(entityId, "Entity id cannot be null!");
		LOG.info("Retrieving entity of type " + entityClass.getSimpleName() + " with id " + entityId);
		
		T entity = null;
		JpaRepository<T, Long> dao = (JpaRepository<T, Long>) getDaoByEntityClass(entityClass);
		entity = dao.findOne(entityId);
		
		if (entity == null) {
			LOG.warn("Entity of type " + entityClass.getSimpleName() + " with id " + entityId + " does not exist!");
			throw new DataDoesNotExistException("Invalid id requested!");
		}
		
		return entity;
	}

	@Override
	@Transactional
	public <T extends Base> void deleteEntity(Long entityId, Class<T> entityClass) throws InvalidDataTypeException, DataDoesNotExistException {
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		Preconditions.checkNotNull(entityId, "Entity id cannot be null!");
		LOG.info("Deleting entity of type " + entityClass.getSimpleName() + " with id " + entityId);
		
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Base> JpaRepository<T, Long> getDaoByEntityClass(Class<T> entityClass) throws InvalidDataTypeException {

		if (entityClass == Execution.class) {
			return (JpaRepository<T, Long>) executionDao;
		} else if (entityClass == ExecutionTemplate.class) {
			return (JpaRepository<T, Long>) executionTemplateDao;
		} else if (entityClass == MaterialTemplate.class) {
			return (JpaRepository<T, Long>) materialTemplateDao;
		} else if (entityClass == ProcedureTemplate.class) {
			return (JpaRepository<T, Long>) procedureTemplateDao;
		} else if (entityClass == Procedure.class) {
			return (JpaRepository<T, Long>) procedureDao;
		} else if (entityClass == Sample.class) {
			return (JpaRepository<T, Long>) sampleDao;
		} else if (entityClass == SampleTemplate.class) {
			return (JpaRepository<T, Long>) sampleTemplateDao;
		} else if (entityClass == Doctor.class) {
			return (JpaRepository<T, Long>) doctorDao;
		} else if (entityClass == User.class) {
			return (JpaRepository<T, Long>) userDao;
		} else if (entityClass == Clinic.class) {
			return (JpaRepository<T, Long>) clinicDao;
		}
		
		LOG.warn("Invalid entity type " + entityClass.getSimpleName());
		throw new InvalidDataTypeException("Invalid data type " + entityClass.getName());
	}

	@Override
	public List<Sample> getProcedureSamples(Long procedureId) {
		return sampleDao.findByProcedureId(procedureId);
	}

	@Override
	public List<Execution> getProcedureExecutions(Long procedureId) {
		return executionDao.findByProcedureId(procedureId);
	}
}
