package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.model.Appointment;
import com.jaab.edelweiss.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/physician")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Creates a new prescription based on an AppointmentDTO payload from the doctor API
     * @param appointmentDTO - the AppointmentDTO payload from the doctor API
     * @return - the appointment data
     */
    @PostMapping(value = "/newAppointment", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Appointment createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
       return appointmentService.createAppointment(appointmentDTO);
    }

    /**
     * Retrieves a list of appointments from the appointment database based on the doctor's name and sends it
     * to the doctor API
     * @param firstName - the first name of the doctor
     * @param lastName - the last name of the doctor
     * @return - the list of the doctor's appointments
     */
    @GetMapping(value = "/myAppointments/{firstName}/{lastName}")
    public List<AppointmentDTO> getAppointmentsByDoctorName(@PathVariable String firstName,
                                                            @PathVariable String lastName) {
        return appointmentService.getAppointmentsByDoctorName(firstName, lastName);
    }

    /**
     * Updates the appointment with the corresponding ID and merges it to the appointment database
     * @param appointmentDTO - the AppointmentDTO payload from the doctor API
     * @param appointmentId - the ID of the appointment
     * @return - the updated appointment
     */
    @PatchMapping(value = "/updateAppointmentInfo/{appointmentId}", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public AppointmentDTO updateAppointmentInfo(@RequestBody AppointmentDTO appointmentDTO,
                                                @PathVariable Long appointmentId) {
        return appointmentService.updateAppointmentInfo(appointmentDTO, appointmentId);
    }
}
