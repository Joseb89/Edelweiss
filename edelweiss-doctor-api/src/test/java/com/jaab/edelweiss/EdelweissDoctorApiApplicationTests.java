package com.jaab.edelweiss;

import com.jaab.edelweiss.controller.*;
import com.jaab.edelweiss.service.DoctorAppointmentService;
import com.jaab.edelweiss.service.DoctorPatientService;
import com.jaab.edelweiss.service.DoctorPrescriptionService;
import com.jaab.edelweiss.service.DoctorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EdelweissDoctorApiApplicationTests {

	@Autowired
	private DoctorController doctorController;

	@Autowired
	private DoctorPatientController doctorPatientController;

	@Autowired
	private DoctorPrescriptionController doctorPrescriptionController;

	@Autowired
	private DoctorAppointmentController doctorAppointmentController;

	@Autowired
	private DoctorService doctorService;

	@Autowired
	private DoctorPatientService doctorPatientService;

	@Autowired
	private DoctorPrescriptionService doctorPrescriptionService;

	@Autowired
	private DoctorAppointmentService doctorAppointmentService;

	@Test
	void contextLoads() {
		assertNotNull(doctorController);
		assertNotNull(doctorPatientController);
		assertNotNull(doctorPrescriptionController);
		assertNotNull(doctorAppointmentController);

		assertNotNull(doctorService);
		assertNotNull(doctorPatientService);
		assertNotNull(doctorPrescriptionService);
		assertNotNull(doctorAppointmentService);
	}

}
