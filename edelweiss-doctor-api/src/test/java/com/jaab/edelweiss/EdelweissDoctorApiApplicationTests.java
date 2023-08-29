package com.jaab.edelweiss;

import com.jaab.edelweiss.controller.DoctorController;
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
	private DoctorService doctorService;

	@Test
	void contextLoads() {
		assertNotNull(doctorController);
		assertNotNull(doctorService);
	}

}
