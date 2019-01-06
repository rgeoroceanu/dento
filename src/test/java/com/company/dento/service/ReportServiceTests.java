package com.company.dento.service;

import com.company.dento.model.business.*;
import com.company.dento.service.exception.CannotGenerateReportException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ReportServiceTests {

	@Autowired
	private ReportService reportService;

	@Test
	public void testCreateOrderReport() throws CannotGenerateReportException {
		final Order order = createOrder();
		final File reportFile = reportService.createOrderReport(order);

		assertNotNull(reportFile);
		assertTrue(reportFile.exists());
		assertTrue(reportFile.getName().startsWith("report"));
	}

	private Order createOrder() {
		final Clinic clinic = new Clinic();
		clinic.setName("Test Clinic");
		final Order order = new Order();
		order.setId(1L);
		order.setDate(LocalDate.now());
		order.setDeliveryDate(LocalDateTime.now());
		order.setDescription("Test Description here text");
		order.setPatient("Gheorghe Ion");
		order.setClinic(clinic);
		final Doctor doctor = new Doctor();
		doctor.setFirstName("Dr. Raul");
		doctor.setLastName("Vasile");
		order.setDoctor(doctor);
		final Color color = new Color();
		color.setName("A2");
		order.setColor(color);
		final JobTemplate jobTemplate1 = new JobTemplate();
		jobTemplate1.setName("Test Job 1");
		final Job job1 = new Job();
		job1.setTemplate(jobTemplate1);
		final JobTemplate jobTemplate2 = new JobTemplate();
		jobTemplate2.setName("Test Job 2");
		final Job job2 = new Job();
		job2.setTemplate(jobTemplate2);
		order.getJobs().addAll(Arrays.asList(job1, job2));
		return order;
	}
}
