package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.model.Appointment;
import com.jaab.edelweiss.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Creates a new prescription based on AppointmentDTO object from the doctor API
     * @param appointmentDTO - the AppointmentDTO object from the doctor API
     * @return - appointment data
     */
    @PostMapping(value = "/physician/newAppointment", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Appointment createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
       return appointmentService.createAppointment(appointmentDTO);
    }
}
