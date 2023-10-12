package com.jaab.edelweiss;

import com.jaab.edelweiss.controller.PatientController;
import com.jaab.edelweiss.dao.PatientRepository;
import com.jaab.edelweiss.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EdelweissPatientApiApplicationTests {

    @Autowired
    private PatientController patientController;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void contextLoads() {
        assertNotNull(patientController);
        assertNotNull(patientService);
        assertNotNull(patientRepository);
    }
}
