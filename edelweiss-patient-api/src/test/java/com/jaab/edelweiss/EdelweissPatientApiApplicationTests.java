package com.jaab.edelweiss;

import com.jaab.edelweiss.controller.PatientController;
import com.jaab.edelweiss.service.PatientService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EdelweissPatientApiApplicationTests {

	@Autowired
	private PatientController patientController;

	@Autowired
	private PatientService patientService;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(patientController);
		Assertions.assertNotNull(patientService);
	}

}
