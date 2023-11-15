package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.service.AppointmentService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/physician")
public class AppointmentController {

    private final AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Creates a new appointment based on an AppointmentDTO payload from the doctor API
     *
     * @param appointmentDTO - the AppointmentDTO payload from the doctor API
     * @return - the appointment data
     */
    @PostMapping(value = "/newAppointment")
    public Mono<AppointmentDTO> createAppointment(@RequestBody AppointmentDTO appointmentDTO) {
        return Mono.just(appointmentService.createAppointment(appointmentDTO));
    }

    /**
     * Retrieves a list of appointments from the appointment database based on the doctor's name and sends it
     * to the doctor API
     *
     * @param firstName - the first name of the doctor
     * @param lastName  - the last name of the doctor
     * @return - the list of the doctor's appointments
     */
    @GetMapping(value = "/myAppointments/{firstName}/{lastName}")
    public Flux<AppointmentDTO> getAppointmentsByDoctorName(@PathVariable String firstName,
                                                            @PathVariable String lastName) {
        return Flux.fromIterable(appointmentService.getAppointmentsByDoctorName(firstName, lastName));
    }

    /**
     * Updates the appointment with the specified ID and merges it to the appointment database
     *
     * @param appointmentDTO - the AppointmentDTO payload from the doctor API
     * @param appointmentId  - the ID of the appointment
     * @return - the updated appointment
     */
    @PatchMapping(value = "/updateAppointmentInfo/{appointmentId}")
    public Mono<AppointmentDTO> updateAppointmentInfo(@RequestBody AppointmentDTO appointmentDTO,
                                                      @PathVariable Long appointmentId) {
        return Mono.just(appointmentService.updateAppointmentInfo(appointmentDTO, appointmentId));
    }

    /**
     * Deletes an appointment from the appointment database based on their ID
     *
     * @param appointmentId - the ID of the appointment
     */
    @DeleteMapping(value = "/deleteAppointment/{appointmentId}")
    public String deleteAppointment(@PathVariable Long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);

        return "Appointment successfully deleted.";
    }
}
