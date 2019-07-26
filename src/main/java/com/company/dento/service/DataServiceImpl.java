package com.company.dento.service;

import com.company.dento.dao.*;
import com.company.dento.dao.specification.MaterialSpecification;
import com.company.dento.dao.specification.OrderSpecification;
import com.company.dento.model.business.*;
import com.company.dento.model.type.Role;
import com.company.dento.model.type.SelectionType;
import com.company.dento.service.exception.DataDoesNotExistException;
import com.company.dento.service.exception.InvalidDataTypeException;
import com.google.common.base.Preconditions;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
    private final MaterialTemplatesDao materialTemplatesDao;
    private final ExecutionTemplateDao executionTemplateDao;
    private final ExecutionDao executionDao;
    private final OrderDao orderDao;
    private final SampleDao sampleDao;
    private final SampleTemplateDao sampleTemplateDao;
    private final DoctorDao doctorDao;
    private final UserDao userDao;
    private final ClinicDao clinicDao;
    private final ToothColorDao toothColorDao;
    private final ToothOptionDao toothOptionDao;
    private final CalendarEventDao calendarEventDao;
    private final GeneralDataDao generalDataDao;
    private final MaterialDao materialDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DataServiceImpl(final JobDao jobDao, final JobTemplateDao jobTemplateDao, final MaterialTemplatesDao materialTemplatesDao,
                           final ExecutionTemplateDao executionTemplateDao, final ExecutionDao executionDao,
                           final OrderDao orderDao, final SampleDao sampleDao, final SampleTemplateDao sampleTemplateDao,
                           final DoctorDao doctorDao, final UserDao userDao, final ClinicDao clinicDao,
                           final ToothColorDao toothColorDao, ToothOptionDao toothOptionDao, final CalendarEventDao calendarEventDao,
                           final GeneralDataDao generalDataDao, final MaterialDao materialDao) {

        this.jobDao = jobDao;
        this.jobTemplateDao = jobTemplateDao;
        this.materialTemplatesDao = materialTemplatesDao;
        this.executionTemplateDao = executionTemplateDao;
        this.executionDao = executionDao;
        this.orderDao = orderDao;
        this.sampleDao = sampleDao;
        this.sampleTemplateDao = sampleTemplateDao;
        this.doctorDao = doctorDao;
        this.userDao = userDao;
        this.clinicDao = clinicDao;
        this.toothColorDao = toothColorDao;
        this.toothOptionDao = toothOptionDao;
        this.calendarEventDao = calendarEventDao;
        this.generalDataDao = generalDataDao;
        this.materialDao = materialDao;
    }

    @PostConstruct
    public void init() throws InvalidDataTypeException {
        createInitialData();
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

        return dao.findAll().stream().filter(this::isNotSoftDeleted).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public <T extends Base> Optional<T> getEntity(final Long entityId, final Class<T> entityClass)
            throws InvalidDataTypeException {

        Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
        Preconditions.checkNotNull(entityId, "Entity id cannot be null!");
        log.info("Retrieving entity of type " + entityClass.getSimpleName() + " with id " + entityId);

        final JpaRepository<T, Long> dao = getDaoByEntityClass(entityClass);

        return dao.findById(entityId).filter(this::isNotSoftDeleted);
    }

    @Override
    @Transactional
    public <T extends Base> void deleteEntity(final Long entityId, final Class<T> entityClass)
            throws InvalidDataTypeException {

        Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
        Preconditions.checkNotNull(entityId, "Entity id cannot be null!");
        log.info("Deleting entity of type " + entityClass.getSimpleName() + " with id " + entityId);

        final JpaRepository<T, Long> dao = getDaoByEntityClass(entityClass);
        dao.deleteById(entityId);
    }

    @Override
    @Transactional
    public <T extends Base & SoftDelete> void softDeleteEntity(final Long entityId, final Class<T> entityClass)
            throws InvalidDataTypeException {

        Preconditions.checkNotNull(entityClass, "Entity class cannot be null!");
        Preconditions.checkNotNull(entityId, "Entity id cannot be null!");
        log.info("Deleting entity of type " + entityClass.getSimpleName() + " with id " + entityId);

        final JpaRepository<T, Long> dao = getDaoByEntityClass(entityClass);
        dao.findById(entityId).ifPresent(item -> {
            item.setDeleted(true);
            item.setActive(false);
            dao.save(item);
        });
    }

    @Override
    public User getUser(final String username) throws DataDoesNotExistException {
        Preconditions.checkNotNull(username, "Username cannot be null!");
        log.info("Retrieve user " + username);

        return userDao.findByUsername(username)
                .filter(this::isNotSoftDeleted)
                .orElseThrow(() -> new DataDoesNotExistException(String
                        .format("Invalid username requested: %s", username)));
    }

    @Transactional
    @Override
    public User saveUserAndEncodePassword(final User user) {
        Preconditions.checkNotNull(user, "User must not be null!");
        log.info("Save user " + user.getUsername());

        final boolean noPasswordEncoding = userDao.findByUsername(user.getUsername())
                .filter(previous -> previous.getPassword().equals(user.getPassword()))
                .isPresent();

        if (!noPasswordEncoding) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

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
        Preconditions.checkNotNull(spec, "Search specification must not be null!");
        log.info("Count by search specification " + spec.toString());

        final JpaSpecificationExecutor dao = (JpaSpecificationExecutor) getDaoByEntityClass(itemClass);
        return Math.toIntExact(dao.count(spec));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CalendarEvent> getCalendarEvents(final LocalDate start, final LocalDate end) {
        return calendarEventDao.findByDateBetween(start, end);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<GeneralData> getGeneralData() {
        return generalDataDao.findOne(Example.of(new GeneralData(), ExampleMatcher.matchingAny().withIgnoreNullValues()));
    }

    @Override
    @Transactional
    public GeneralData saveGeneralData(final GeneralData generalData) {
        this.getGeneralData().ifPresent(prev -> generalData.setId(prev.getId()));
        return generalDataDao.save(generalData);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getAllTechnicians() {
        return userDao.findAll().stream()
                .filter(u -> u.getRoles().contains(Role.TECHNICIAN))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public double getExecutionsPriceTotal(final OrderSpecification spec) {
        Preconditions.checkNotNull(spec, "Search specification must not be null!");
        log.info("Calculate total execution price!");

        final Long technicianId = spec.getTechnician() != null ? spec.getTechnician().getId() : null;
        return orderDao.calculateExecutionsPriceTotal(technicianId, spec.getFinalized(),
                spec.getStartDate(), spec.getEndDate());
    }

    @Override
    @Transactional(readOnly = true)
    public double getJobsPriceTotal(final OrderSpecification spec) {
        Preconditions.checkNotNull(spec, "Search specification must not be null!");
        log.info("Calculate total orders price!");

        final Long doctorId = spec.getDoctor() != null ? spec.getDoctor().getId() : null;
        final Long clinicId = spec.getClinic() != null ? spec.getClinic().getId() : null;
        return orderDao.calculateJobsPriceTotal(spec.getId(), spec.getStartDate(), spec.getEndDate(),
                spec.getPatient(), doctorId, clinicId, spec.getFinalized(), spec.getPaid());
    }

    @Override
    @Transactional(readOnly = true)
    public double getMaterialPriceTotal(final MaterialSpecification spec) {
        Preconditions.checkNotNull(spec, "Search specification must not be null!");
        log.info("Calculate total materials price!");

        final Long doctorId = spec.getDoctor() != null ? spec.getDoctor().getId() : null;
        final Long clinicId = spec.getClinic() != null ? spec.getClinic().getId() : null;
        final Long templateId = spec.getTemplate() != null ? spec.getTemplate().getId() : null;
        final Long jobId = spec.getJob() != null ? spec.getJob().getId() : null;
        return materialDao.calculateMaterialsPriceTotal(templateId, jobId, doctorId, clinicId,
                spec.getStartDate(), spec.getEndDate());
    }

    @Override
    @Transactional
    public Order saveOrder(final Order order) {
        Preconditions.checkNotNull(order, "Order cannot be null!");
        log.info("Save order {}", order);

        final Order saved = orderDao.save(order);
        order.getJobs().forEach(j -> updateExecutions(j.getExecutions()));
        order.getJobs().forEach(j -> updateMaterials(j.getMaterials()));
        return saved;
    }

    private void updateExecutions(final Set<Execution> executions) {
        executions.forEach(execution -> {
            final float price = execution.getTemplate().getIndividualPrices().stream()
                    .filter(p -> execution.getTechnician() != null && p.getTechnician() != null)
                    .filter(p -> p.getTechnician().getId().equals(execution.getTechnician().getId()))
                    .findFirst()
                    .map(ExecutionPrice::getPrice)
                    .orElse(execution.getTemplate().getStandardPrice());
            execution.setPrice(price);

            final int count = SelectionType.SIMPLE.equals(execution.getJob().getTemplate().getSelectionType()) ?
                    execution.getJob().getTeeth().size() : 1;

            execution.setCount(count);
        });
    }

    private void updateMaterials(final Set<Material> materials) {
        materials.forEach(material -> {
            final int count = material.getTemplate().isPerJob() ? 1 : material.getJob().getTeeth().size();
            material.setQuantity(material.getQuantity() * count);
        });
    }

    private List<Sort.Order> extractSortOrders(final Map<String, Boolean> sortOrder) {
        return sortOrder.entrySet().stream()
                .map(e -> new Sort.Order(e.getValue() ? Sort.Direction.ASC : Sort.Direction.DESC, e.getKey()))
                .collect(Collectors.toList());
    }

    private <T extends Base> boolean isNotSoftDeleted(final T item) {
        return !(item instanceof SoftDelete && ((SoftDelete) item).isDeleted());
    }

    @SuppressWarnings("unchecked")
    private <T extends Base> JpaRepository<T, Long> getDaoByEntityClass(final Class<T> entityClass)
            throws InvalidDataTypeException {

        if (entityClass == Job.class) {
            return (JpaRepository<T, Long>) jobDao;
        } else if (entityClass == JobTemplate.class) {
            return (JpaRepository<T, Long>) jobTemplateDao;
        } else if (entityClass == MaterialTemplate.class) {
            return (JpaRepository<T, Long>) materialTemplatesDao;
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
        } else if (entityClass == ToothColor.class) {
            return (JpaRepository<T, Long>) toothColorDao;
        } else if (entityClass == ToothOption.class) {
            return (JpaRepository<T, Long>) toothOptionDao;
        } else if (entityClass == Material.class) {
            return (JpaRepository<T, Long>) materialDao;
        }

        log.warn("Invalid entity type " + entityClass.getSimpleName());
        throw new InvalidDataTypeException("Invalid data type " + entityClass.getName());
    }

    private void createInitialData() {
        // add initial user
        if (userDao.findAll().isEmpty()) {
            final User user = new User();
            user.setFirstName("A.");
            user.setLastName("Administrator");
            user.setUsername("dento");
            user.setPassword("dentoadmin");
            user.setRoles(new HashSet<>(Arrays.asList(Role.USER, Role.ADMIN, Role.TECHNICIAN)));
            saveUserAndEncodePassword(user);
        }
    }
}
