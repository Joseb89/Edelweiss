package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.service.DoctorAppointmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This class is a controller for the endpoints that create and maintain appointment data
 *
 * @author Joseph Barr
 */
@RestController
@RequestMapping(value = "/physician")
public class DoctorAppointmentController {

    private final DoctorAppointmentService doctorAppointmentService;

    public DoctorAppointmentController(DoctorAppointmentService doctorAppointmentService) {
        this.doctorAppointmentService = doctorAppointmentService;
    }

    /**
     * Sends an AppointmentDTO payload to the appointment API
     *
     * @param appointment - the AppointmentDTO payload
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with the new appointment
     */
    @PostMapping(value = "/{physicianId}/newAppointment", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<AppointmentDTO>> createAppointment(@RequestBody AppointmentDTO appointment,
                                                                  @PathVariable Long physicianId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorAppointmentService.createAppointment(appointment, physicianId));
    }

    /**
     * Retrieves the specified doctor's appointments from the appointment API
     *
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with the list of the doctor's appointments
     */
    @GetMapping(value = "/{physicianId}/myAppointments")
    public ResponseEntity<Flux<AppointmentDTO>> getAppointments(@PathVariable Long physicianId) {
        return ResponseEntity.ok(doctorAppointmentService.getAppointments(physicianId));
    }

    /**
     * Updates an appointment with the corresponding ID and sends it to the appointment API
     *
     * @param appointmentDTO - the AppointmentDTO payload containing the updated information
     * @param appointmentId  - the ID of the appointment
     * @return - HTTP status response with the updated information
     */
    @PatchMapping(value = "/updateAppointmentInfo/{appointmentId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<AppointmentDTO>> updateAppointmentInfo(@RequestBody AppointmentDTO appointmentDTO,
                                                                      @PathVariable Long appointmentId) {
        return ResponseEntity.ok(doctorAppointmentService.updateAppointmentInfo(appointmentDTO, appointmentId));
    }

    /**
     * Sends a DELETE request to the appointment API to delete the appointment with the specified ID
     *
     * @param appointmentId - the ID of the appointment
     * @return - HTTP status response
     */
    @DeleteMapping(value = "/deleteAppointment/{appointmentId}")
    public ResponseEntity<Mono<Void>> deleteAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(doctorAppointmentService.deleteAppointment(appointmentId));
    }
}
