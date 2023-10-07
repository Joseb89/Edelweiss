package com.jaab.edelweiss;

import com.jaab.edelweiss.controller.PrescriptionController;
import com.jaab.edelweiss.dao.PrescriptionRepository;
import com.jaab.edelweiss.service.PrescriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EdelweissPrescriptionApiApplicationTests {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private PrescriptionService prescriptionService;

    @Autowired
    private PrescriptionController prescriptionController;

    @Test
    void contextLoads() {
        assertNotNull(prescriptionRepository);
        assertNotNull(prescriptionService);
        assertNotNull(prescriptionController);
    }
}
