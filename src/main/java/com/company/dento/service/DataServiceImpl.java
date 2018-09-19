package com.company.dento.service;

import com.company.dento.dao.*;
import com.company.dento.dao.specification.ExecutionSpecification;
import com.company.dento.model.business.*;
import com.company.dento.model.type.MeasurementUnit;
import com.company.dento.model.type.Role;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.service.exception.InvalidDataTypeException;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DataServiceImpl implements DataService {
	
	private final JobDao jobDao;
	private final JobTemplateDao jobTemplateDao;
	private final MaterialTemplateDao materialTemplateDao;
	private final ExecutionTemplateDao executionTemplateDao;
	private final ExecutionDao executionDao;
	private final OrderTemplateDao orderTemplateDao;
	private final OrderDao orderDao;
	private final SampleDao sampleDao;
	private final SampleTemplateDao sampleTemplateDao;
	private final DoctorDao doctorDao;
	private final UserDao userDao;
	private final ClinicDao clinicDao;
	private final PasswordEncoder passwordEncoder;

	public DataServiceImpl(final JobDao jobDao, final JobTemplateDao jobTemplateDao,
						   final MaterialTemplateDao materialTemplateDao,
						   final ExecutionTemplateDao executionTemplateDao, final ExecutionDao executionDao,
						   final OrderTemplateDao orderTemplateDao, final OrderDao orderDao, final SampleDao sampleDao,
						   final SampleTemplateDao sampleTemplateDao, final DoctorDao doctorDao, final UserDao userDao,
						   final ClinicDao clinicDao, final PasswordEncoder passwordEncoder) {

		this.jobDao = jobDao;
		this.jobTemplateDao = jobTemplateDao;
		this.materialTemplateDao = materialTemplateDao;
		this.executionTemplateDao = executionTemplateDao;
		this.executionDao = executionDao;
		this.orderTemplateDao = orderTemplateDao;
		this.orderDao = orderDao;
		this.sampleDao = sampleDao;
		this.sampleTemplateDao = sampleTemplateDao;
		this.doctorDao = doctorDao;
		this.userDao = userDao;
		this.clinicDao = clinicDao;
		this.passwordEncoder = passwordEncoder;
	}

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
			JobTemplate jobTemplate1 = new JobTemplate();
			jobTemplate1.setName("Executie Ceramica");
			JobTemplate jobTemplate2 = new JobTemplate();
			jobTemplate2.setName("Executie CAD");
			saveEntity(jobTemplate1);
			saveEntity(jobTemplate2);
			
			ExecutionTemplate executionTemplate1 = new ExecutionTemplate();
			executionTemplate1.setName("Executie Ceramica");
            ExecutionTemplate executionTemplate2 = new ExecutionTemplate();
			executionTemplate2.setName("Executie CAD");
			saveEntity(executionTemplate1);
			saveEntity(executionTemplate2);
			
			SampleTemplate sampleTemplate1 = new SampleTemplate();
			sampleTemplate1.setName("Proba Ceramica");
			saveEntity(sampleTemplate1);
			
			OrderTemplate procedureTemplate = new OrderTemplate();
			procedureTemplate.setName("Ceramica Zr");
			procedureTemplate.setPrice(100);
			procedureTemplate.getExecutions().add(jobTemplate1);
			procedureTemplate.getExecutions().add(jobTemplate2);
			procedureTemplate.getSamples().add(sampleTemplate1);
			saveEntity(procedureTemplate);
			
			MaterialTemplate material = new MaterialTemplate();
			material.setName("Material 1");
			material.setMeasurementUnit(MeasurementUnit.CM);
			material.setPerJob(true);
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
			
			Order order = new Order();
			order.setTemplate(procedureTemplate);
			order.setDeliveryDate(LocalDateTime.now());
			order.setDoctor(doctor);
			order.setPrice(111);
			order.setPatient("Gheorghe");
			saveEntity(order);
			
			Job job1 = new Job();
			job1.setTechnician(user);
			job1.setTemplate(jobTemplate1);
			job1.setOrder(order);
            saveEntity(job1);
			
			Job job2 = new Job();
			job2.setTechnician(user);
			job2.setTemplate(jobTemplate1);
			job2.setOrder(order);
            saveEntity(job2);
			
			Job job3 = new Job();
			job3.setTechnician(user);
			job3.setTemplate(jobTemplate1);
			job3.setOrder(order);
            saveEntity(job3);

			Execution execution1 = new Execution();
			execution1.setCount(1);
			execution1.setJob(job1);
			execution1.setPrice(executionTemplate1.getStandardPrice());
			execution1.setTemplate(executionTemplate1);
            saveEntity(execution1);

            Execution execution2 = new Execution();
            execution2.setCount(2);
            execution2.setJob(job2);
            execution2.setPrice(executionTemplate2.getStandardPrice());
            execution2.setTemplate(executionTemplate2);
            saveEntity(execution2);

            for (int i=0; i<200; i++) {
				Execution execution3 = new Execution();
				execution3.setCount(1);
				execution3.setJob(job3);
				execution3.setPrice(executionTemplate2.getStandardPrice());
				execution3.setTemplate(executionTemplate2);
				saveEntity(execution3);
			}

			Sample sample = new Sample();
			sample.setTemplate(sampleTemplate1);
			sample.setOrder(order);
			
			order.getSamples().add(sample);
			//order.getJobs().addAll(Arrays.asList(execution1, execution2, execution3));
			
			saveEntity(order);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Transactional
	public <T extends Base> T saveEntity(final T entity) throws InvalidDataTypeException {
		Preconditions.checkNotNull(entity, "Entity cannot be null!");
		log.info("Saving entity of type " + entity.getClass().getSimpleName() + ": " + entity);
		
		JpaRepository<T, Long> dao = (JpaRepository<T, Long>) getDaoByEntityClass(entity.getClass());
		
		return dao.saveAndFlush(entity);
	}

	@Override
	@Transactional
	public <T extends Base> List<T> getAll(final Class<T> entityClass) throws InvalidDataTypeException {
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		log.info("Retrieving all entitities of type " + entityClass.getSimpleName());
		
		JpaRepository<T, Long> dao = getDaoByEntityClass(entityClass);
		
		return dao.findAll();
	}

	@Override
	@Transactional
	public <T extends Base> T getEntity(final Long entityId, final Class<T> entityClass)
			throws InvalidDataTypeException, DataDoesNotExistException {
		
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		Preconditions.checkNotNull(entityId, "Entity id cannot be null!");
		log.info("Retrieving entity of type " + entityClass.getSimpleName() + " with id " + entityId);
		
		final JpaRepository<T, Long> dao = getDaoByEntityClass(entityClass);
		
		return dao.findById(entityId)
				.orElseThrow(()-> new DataDoesNotExistException(String
						.format("Invalid id requested: %s", entityId)));
	}

	@Override
	@Transactional
	public <T extends Base> void deleteEntity(final Long entityId, final Class<T> entityClass)
			throws InvalidDataTypeException, DataDoesNotExistException {
		
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		Preconditions.checkNotNull(entityId, "Entity id cannot be null!");
		log.info("Deleting entity of type " + entityClass.getSimpleName() + " with id " + entityId);
		
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Base> JpaRepository<T, Long> getDaoByEntityClass(final Class<T> entityClass)
			throws InvalidDataTypeException {
		
		if (entityClass == Job.class) {
			return (JpaRepository<T, Long>) jobDao;
		} else if (entityClass == JobTemplate.class) {
			return (JpaRepository<T, Long>) jobTemplateDao;
		} else if (entityClass == MaterialTemplate.class) {
			return (JpaRepository<T, Long>) materialTemplateDao;
		} else if (entityClass == OrderTemplate.class) {
			return (JpaRepository<T, Long>) orderTemplateDao;
		} else if (entityClass == Order.class) {
			return (JpaRepository<T, Long>) orderDao;
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
		} else if (entityClass == Execution.class) {
			return (JpaRepository<T, Long>) executionDao;
		} else if (entityClass == ExecutionTemplate.class) {
			return (JpaRepository<T, Long>) executionTemplateDao;
		}
		
		log.warn("Invalid entity type " + entityClass.getSimpleName());
		throw new InvalidDataTypeException("Invalid data type " + entityClass.getName());
	}

	@Override
	public List<Sample> getProcedureSamples(final Long procedureId) {
		Preconditions.checkNotNull(procedureId, "Procedure id cannot be null!");
		log.info("Retrieve samples for procedure " + procedureId);
		return sampleDao.findByOrderId(procedureId);
	}

	@Override
	public List<Job> getProcedureExecutions(final Long procedureId) {
		Preconditions.checkNotNull(procedureId, "Procedure id cannot be null!");
		log.info("Retrieve executions for procedure " + procedureId);
		return jobDao.findByOrderId(procedureId);
	}

	@Override
	public User getUser(final String username) throws DataDoesNotExistException {
		Preconditions.checkNotNull(username, "Username cannot be null!");
		log.info("Retrieve user " + username);
		
		return userDao.findByUsername(username)
				.orElseThrow(() -> new DataDoesNotExistException(String
						.format("Invalid username requested: %s", username)));
	}
	
	@Transactional
	@Override
	public User saveUserAndEncodePassword(final User user) {
		Preconditions.checkNotNull(user, "User must not be null!");
		log.info("Save user " + user.getUsername());

		user.setPassword(passwordEncoder.encode(user.getPassword()));
		return userDao.saveAndFlush(user);
	}

	@Transactional(readOnly = true)
	@Override
	public <T extends Base, V> List<T> getByCriteria(final Class<T> itemClass,
													 final V criteria, final int offset,
													 final int limit, final Map<String, Boolean> sortOrder) {

	    Preconditions.checkNotNull(criteria, "Search criteria must not be null!");
		log.info("Requested all executions by search criteria " + criteria.toString());

		final int page = offset / limit;
		final Sort sort = Sort.by(extractSortOrders(sortOrder));
		final PageRequest pageRequest = PageRequest.of(page, limit, sort);
		final JpaSpecificationExecutor dao = (JpaSpecificationExecutor) getDaoByEntityClass(itemClass);
		final Specification specification = getSpecification(itemClass, criteria);
		return dao.findAll(specification, pageRequest).getContent();
	}

	@Transactional(readOnly = true)
	@Override
	public <T extends Base, V>  int countByCriteria(final Class<T> itemClass, final V criteria) {
		Preconditions.checkNotNull(criteria, "Search criteria must not be null!");
		log.info("Count by search criteria " + criteria.toString());

		final JpaSpecificationExecutor dao = (JpaSpecificationExecutor) getDaoByEntityClass(itemClass);
		final Specification specification = getSpecification(itemClass, criteria);
        return Math.toIntExact(dao.count(specification));
	}

	private List<Sort.Order> extractSortOrders(final Map<String, Boolean> sortOrder) {
		return sortOrder.entrySet().stream()
				.map(e -> new Sort.Order(e.getValue() ? Sort.Direction.ASC : Sort.Direction.DESC, e.getKey()))
				.collect(Collectors.toList());
	}

    private <T extends Base, V> Specification getSpecification(final Class<T> itemClass, V criteria) {
        if (itemClass == Execution.class) {
            return new ExecutionSpecification((ExecutionCriteria) criteria);
        } else {
            throw new IllegalArgumentException(String.format("No Specification found for class %s", itemClass));
        }
    }
}
