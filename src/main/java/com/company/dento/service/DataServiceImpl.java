package com.company.dento.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
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

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class DataServiceImpl implements DataService {
	
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
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@PostConstruct
	public void init() throws InvalidDataTypeException {
		// add initial user
		if (userDao.findAll().isEmpty()) {
			User user = new User();
			user.setFirstName("Admin");
			user.setLastName("Admin");
			user.setUsername("admin");
			user.setPassword("admin1234");
			user.setRoles(new HashSet<>(Arrays.asList(Role.ADMIN)));
			saveUserAndEncodePassword(user);
			
			// TODO remove this
			ExecutionTemplate executionTemplate1 = new ExecutionTemplate();
			executionTemplate1.setEstimatedDuration(1);
			executionTemplate1.setName("Executie Ceramica");
			ExecutionTemplate executionTemplate2 = new ExecutionTemplate();
			executionTemplate2.setEstimatedDuration(2);
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
			
			Execution execution1 = new Execution();
			execution1.setTechnician(user);
			execution1.setTemplate(executionTemplate1);
			execution1.setProcedure(procedure);
			
			Execution execution2 = new Execution();
			execution2.setTechnician(user);
			execution2.setTemplate(executionTemplate1);
			execution2.setProcedure(procedure);
			
			Execution execution3 = new Execution();
			execution3.setTechnician(user);
			execution3.setTemplate(executionTemplate1);
			execution3.setProcedure(procedure);
			
			Sample sample = new Sample();
			sample.setTemplate(sampleTemplate1);
			sample.setProcedure(procedure);
			
			procedure.getSamples().add(sample);
			procedure.getExecutions().addAll(Arrays.asList(execution1, execution2, execution3));
			saveEntity(procedure);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public <T extends Base> T saveEntity(T entity) throws InvalidDataTypeException {
		Preconditions.checkNotNull(entity, "Entity cannot be null!");
		log.info("Saving entity of type " + entity.getClass().getSimpleName() + ": " + entity);
		
		JpaRepository<T, Long> dao = (JpaRepository<T, Long>) getDaoByEntityClass(entity.getClass());
		
		return dao.saveAndFlush(entity);
	}

	@Override
	@Transactional
	public <T extends Base> List<T> getAll(Class<T> entityClass) throws InvalidDataTypeException {
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		log.info("Retrieving all entitities of type " + entityClass.getSimpleName());
		
		JpaRepository<T, Long> dao = (JpaRepository<T, Long>) getDaoByEntityClass(entityClass);
		
		return dao.findAll();
	}

	@Override
	@Transactional
	public <T extends Base> T getEntity(Long entityId, Class<T> entityClass) 
			throws InvalidDataTypeException, DataDoesNotExistException {
		
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		Preconditions.checkNotNull(entityId, "Entity id cannot be null!");
		log.info("Retrieving entity of type " + entityClass.getSimpleName() + " with id " + entityId);
		
		final JpaRepository<T, Long> dao = (JpaRepository<T, Long>) getDaoByEntityClass(entityClass);
		
		return dao.findById(entityId)
				.orElseThrow(()-> new DataDoesNotExistException(String
						.format("Invalid id requested: ", entityId)));
	}

	@Override
	@Transactional
	public <T extends Base> void deleteEntity(Long entityId, Class<T> entityClass) 
			throws InvalidDataTypeException, DataDoesNotExistException {
		
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		Preconditions.checkNotNull(entityId, "Entity id cannot be null!");
		log.info("Deleting entity of type " + entityClass.getSimpleName() + " with id " + entityId);
		
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Base> JpaRepository<T, Long> getDaoByEntityClass(Class<T> entityClass) 
			throws InvalidDataTypeException {
		
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
		
		log.warn("Invalid entity type " + entityClass.getSimpleName());
		throw new InvalidDataTypeException("Invalid data type " + entityClass.getName());
	}

	@Override
	public List<Sample> getProcedureSamples(Long procedureId) {
		Preconditions.checkNotNull(procedureId, "Procedure id cannot be null!");
		log.info("Retrieve samples for procedure " + procedureId);
		return sampleDao.findByProcedureId(procedureId);
	}

	@Override
	public List<Execution> getProcedureExecutions(Long procedureId) {
		Preconditions.checkNotNull(procedureId, "Procedure id cannot be null!");
		log.info("Retrieve executions for procedure " + procedureId);
		return executionDao.findByProcedureId(procedureId);
	}

	@Override
	public User getUser(String username) throws DataDoesNotExistException {
		Preconditions.checkNotNull(username, "Username cannot be null!");
		log.info("Retrieve user " + username);
		
		return userDao.findByUsername(username)
				.orElseThrow(() -> new DataDoesNotExistException(String
						.format("Invalid username requested: ", username)));
	}
	
	@Transactional
	@Override
	public User saveUserAndEncodePassword(User user) {
		Preconditions.checkNotNull(user, "User must not be null!");
		log.info("Save user " + user.getUsername());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userDao.saveAndFlush(user);
	}
}
