package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.*;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This class is a controller for the endpoints for creating and maintaining physician data
 *
 * @author Joseph Barr
 */
@RestController
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * Saves a new doctor to the doctor database and sends the data to the user API
     * @param doctor - the Doctor payload
     * @return - HTTP status response with the ID of the doctor
     */
    @PostMapping(value = "/newPhysician", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPhysician(@RequestBody Doctor doctor) {
        UserDTO newDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDoctor.getId());
    }

    /**
     * Sends an appointment payload to the appointment API
     * @param appointment - the AppointmentDTO payload
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with the appointment payload
     */
    @PostMapping(value = "/physician/{physicianId}/newAppointment", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<AppointmentDTO>> createAppointment(@RequestBody AppointmentDTO appointment,
                                                                  @PathVariable Long physicianId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.createAppointment(appointment, physicianId));
    }

    /**
     * Retrieves the specified doctor's appointments from the appointment API
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with the list of the doctor's appointments
     */
    @GetMapping(value = "/physician/{physicianId}/myAppointments")
    public ResponseEntity<Flux<AppointmentDTO>> getAppointments(@PathVariable Long physicianId) {
        return ResponseEntity.ok(doctorService.getAppointments(physicianId));
    }

    /**
     * Updates the doctor's information and merges it to the doctor database
     * @param doctor - the Doctor payload containing the updated information
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with the updated information
     */
    @PatchMapping(value = "/physician/{physicianId}/updatePhysicianInfo",
                    consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UserDTO>> updateDoctorInfo(@RequestBody Doctor doctor,
                                                          @PathVariable Long physicianId) {
        return ResponseEntity.ok((doctorService.updateUserInfo(doctor, physicianId)));
    }

    /**
     * Updates an appointment with the corresponding ID and sends it to the appointment API
     * @param appointmentDTO - the AppointmentDTO payload containing the updated information
     * @param appointmentId - the ID of the appointment
     * @return - HTTP status response with the updated information
     */
    @PatchMapping(value = "/physician/updateAppointmentInfo/{appointmentId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<AppointmentDTO>> updateAppointmentInfo(@RequestBody AppointmentDTO appointmentDTO,
                                                                      @PathVariable Long appointmentId) {
        return ResponseEntity.ok(doctorService.updateAppointmentInfo(appointmentDTO, appointmentId));
    }

    /**
     * Deletes a doctor from the doctor database and sends a DELETE request to the user API to delete the user
     * with the corresponding ID
     * @param physicianId - the ID of the doctor
     * @return - the DELETE request
     */
    @DeleteMapping(value = "/physician/deleteDoctor/{physicianId}")
    public ResponseEntity<Mono<Void>> deleteDoctor(@PathVariable Long physicianId) {
        return ResponseEntity.ok(doctorService.deleteUser(physicianId));
    }

    /**
     * Sends a DELETE request to the appointment API to delete the appointment with the specified ID
     * @param appointmentId - the ID of the appointment
     * @return - the DELETE request
     */
    @DeleteMapping(value = "/physician/deleteAppointment/{appointmentId}")
    public ResponseEntity<Mono<Void>> deleteAppointment(@PathVariable Long appointmentId) {
        return ResponseEntity.ok(doctorService.deleteAppointment(appointmentId));
    }
}
