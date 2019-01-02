package com.company.dento.service;

import com.company.dento.dao.*;
import com.company.dento.model.business.*;
import com.company.dento.model.type.MeasurementUnit;
import com.company.dento.model.type.Role;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.service.exception.InvalidDataTypeException;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class DataServiceImpl implements DataService {
	
	private final JobDao jobDao;
	private final JobTemplateDao jobTemplateDao;
	private final MaterialTemplateDao materialTemplateDao;
	private final ExecutionTemplateDao executionTemplateDao;
	private final ExecutionDao executionDao;
	private final OrderDao orderDao;
	private final SampleDao sampleDao;
	private final SampleTemplateDao sampleTemplateDao;
	private final DoctorDao doctorDao;
	private final UserDao userDao;
	private final ClinicDao clinicDao;
	private final ColorDao colorDao;
	private final PasswordEncoder passwordEncoder;

	public DataServiceImpl(final JobDao jobDao, final JobTemplateDao jobTemplateDao,
						   final MaterialTemplateDao materialTemplateDao,
						   final ExecutionTemplateDao executionTemplateDao, final ExecutionDao executionDao,
						   final OrderDao orderDao, final SampleDao sampleDao,
						   final SampleTemplateDao sampleTemplateDao, final DoctorDao doctorDao, final UserDao userDao,
						   final ClinicDao clinicDao, final PasswordEncoder passwordEncoder, final ColorDao colorDao) {

		this.jobDao = jobDao;
		this.jobTemplateDao = jobTemplateDao;
		this.materialTemplateDao = materialTemplateDao;
		this.executionTemplateDao = executionTemplateDao;
		this.executionDao = executionDao;
		this.orderDao = orderDao;
		this.sampleDao = sampleDao;
		this.sampleTemplateDao = sampleTemplateDao;
		this.doctorDao = doctorDao;
		this.userDao = userDao;
		this.clinicDao = clinicDao;
		this.passwordEncoder = passwordEncoder;
		this.colorDao = colorDao;
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
			order.setDate(LocalDate.now());
			order.setDoctor(doctor);
			order.setPatient("Gheorghe");
			order.setClinic(clinic);
			order.setPrice(450);
			saveEntity(order);


			Order order2 = new Order();
			order2.setDate(LocalDate.now());
			order2.setDoctor(doctor);
			order2.setPrice(111);
			order2.setPatient("Gheorghe");
			order2.setClinic(clinic);
			saveEntity(order2);

			Execution execution1 = new Execution();
			execution1.setCount(1);
			execution1.setPrice(executionTemplate1.getStandardPrice());
			execution1.setTemplate(executionTemplate1);
			execution1.setTechnician(user);

			Execution execution2 = new Execution();
			execution2.setCount(2);
			execution2.setPrice(executionTemplate2.getStandardPrice());
			execution2.setTemplate(executionTemplate2);
			execution2.setTechnician(user);

			for (int i=0; i<200; i++) {
				Execution execution3 = new Execution();
				execution3.setCount(1);
				execution3.setPrice(executionTemplate2.getStandardPrice());
				execution3.setTemplate(executionTemplate2);
				execution3.setTechnician(user);
			}

			Sample sample = new Sample();
			sample.setTemplate(sampleTemplate1);

			Job job1 = new Job();
			job1.setTemplate(jobTemplate1);
			job1.setOrder(order);
			job1.setPrice(100);
			job1.setExecutions(Arrays.asList(execution1));
			execution1.setJob(job1);

			sample.setJob(job1);

			job1.getSamples().add(sample);
            saveEntity(job1);
			
			Job job2 = new Job();
			job2.setTemplate(jobTemplate1);
			job2.setOrder(order);
			job2.setPrice(150);
			job2.setExecutions(Arrays.asList(execution2));
			execution2.setJob(job2);
            saveEntity(job2);
			
			Job job3 = new Job();

			job3.setTemplate(jobTemplate1);
			job3.setOrder(order2);
			job3.setPrice(200);

            saveEntity(job3);
			
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
	@Transactional(readOnly = true)
	public <T extends Base> List<T> getAll(final Class<T> entityClass) throws InvalidDataTypeException {
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		log.info("Retrieving all entitities of type " + entityClass.getSimpleName());
		
		JpaRepository<T, Long> dao = getDaoByEntityClass(entityClass);
		
		return dao.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public <T extends Base> Optional<T> getEntity(final Long entityId, final Class<T> entityClass)
			throws InvalidDataTypeException {
		
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		Preconditions.checkNotNull(entityId, "Entity id cannot be null!");
		log.info("Retrieving entity of type " + entityClass.getSimpleName() + " with id " + entityId);
		
		final JpaRepository<T, Long> dao = getDaoByEntityClass(entityClass);
		
		return dao.findById(entityId);
	}

	@Override
	@Transactional
	public <T extends Base> void deleteEntity(final Long entityId, final Class<T> entityClass)
			throws InvalidDataTypeException, DataDoesNotExistException {
		
		Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
		Preconditions.checkNotNull(entityId, "Entity id cannot be null!");
		log.info("Deleting entity of type " + entityClass.getSimpleName() + " with id " + entityId);

		final JpaRepository<T, Long> dao = getDaoByEntityClass(entityClass);
		dao.deleteById(entityId);
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
		} else if (entityClass == Color.class) {
			return (JpaRepository<T, Long>) colorDao;
		}
		
		log.warn("Invalid entity type " + entityClass.getSimpleName());
		throw new InvalidDataTypeException("Invalid data type " + entityClass.getName());
	}

	@Override
	public List<Sample> getOrderSamples(final Long orderId) {
		Preconditions.checkNotNull(orderId, "Order id cannot be null!");
		log.info("Retrieve samples for order " + orderId);
		return sampleDao.findByJobOrderId(orderId);
	}

	@Override
	public List<Job> getOrderExecutions(final Long orderId) {
		Preconditions.checkNotNull(orderId, "Order id cannot be null!");
		log.info("Retrieve executions for order " + orderId);
		return jobDao.findByOrderId(orderId);
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
	public <T extends Base> List<T> getByCriteria(final Class<T> itemClass,
												  final Specification<T> spec, final int offset,
												  final int limit, final Map<String, Boolean> sortOrder) {

	    Preconditions.checkNotNull(spec, "Search criteria must not be null!");
		log.info("Requested all executions by search criteria " + spec.toString());

		final Sort sort = Sort.by(extractSortOrders(sortOrder));
		final PageableRepository dao = (PageableRepository) getDaoByEntityClass(itemClass);
		return dao.findAll(spec, offset, limit, sort);
	}

	@Transactional(readOnly = true)
	@Override
	public <T extends Base>  int countByCriteria(final Class<T> itemClass, final Specification<T> spec) {
		Preconditions.checkNotNull(spec, "Search criteria must not be null!");
		log.info("Count by search criteria " + spec.toString());

		final JpaSpecificationExecutor dao = (JpaSpecificationExecutor) getDaoByEntityClass(itemClass);
        return Math.toIntExact(dao.count(spec));
	}

	private List<Sort.Order> extractSortOrders(final Map<String, Boolean> sortOrder) {
		return sortOrder.entrySet().stream()
				.map(e -> new Sort.Order(e.getValue() ? Sort.Direction.ASC : Sort.Direction.DESC, e.getKey()))
				.collect(Collectors.toList());
	}
}
