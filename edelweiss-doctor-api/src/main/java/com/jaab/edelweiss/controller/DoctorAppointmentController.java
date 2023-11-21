package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentException;
import com.jaab.edelweiss.service.DoctorAppointmentService;
import org.springframework.http.HttpStatus;
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
     * @return - HTTP status response with the new appointment
     */
    @PostMapping(value = "/newAppointment")
    public ResponseEntity<Mono<AppointmentDTO>> createAppointment(@RequestBody AppointmentDTO appointment) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorAppointmentService.createAppointment(appointment));
    }

    /**
     * Retrieves the specified doctor's appointments from the appointment API
     *
     * @return - HTTP status response with the list of the doctor's appointments
     */
    @GetMapping(value = "/myAppointments")
    public ResponseEntity<Flux<AppointmentDTO>> getAppointments() {
        return ResponseEntity.ok(doctorAppointmentService.getAppointments());
    }

    /**
     * Updates an appointment with the corresponding ID and sends it to the appointment API
     *
     * @param appointmentDTO - the AppointmentDTO payload containing the updated information
     * @param appointmentId  - the ID of the appointment
     * @return - HTTP status response with the updated information
     */
    @PatchMapping(value = "/updateAppointmentInfo/{appointmentId}")
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
    public ResponseEntity<Mono<String>> deleteAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(doctorAppointmentService.deleteAppointment(appointmentId));
    }

    /**
     * Handles AppointmentException errors when creating and updating appointment data
     *
     * @param e - the AppointmentException object
     * @return - HTTP status response containing the error message
     */
    @ExceptionHandler(AppointmentException.class)
    private ResponseEntity<String> handleAppointmentError(AppointmentException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
