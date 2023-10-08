package com.jaab.edelweiss;

import com.jaab.edelweiss.controller.AppointmentController;
import com.jaab.edelweiss.dao.AppointmentRepository;
import com.jaab.edelweiss.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EdelweissAppointmentApiApplicationTests {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AppointmentController appointmentController;

    @Test
    void contextLoads() {
        assertNotNull(appointmentRepository);
        assertNotNull(appointmentService);
        assertNotNull(appointmentController);
    }
}
