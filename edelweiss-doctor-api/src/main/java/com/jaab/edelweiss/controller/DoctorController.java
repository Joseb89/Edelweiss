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
     * Sends a prescription payload to the prescription API
     * @param prescription - the PrescriptionDTO payload
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with the prescription payload
     */
    @PostMapping(value = "/physician/{physicianId}/newPrescription", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<PrescriptionDTO>> createPrescription(
                                                   @RequestBody PrescriptionDTO prescription,
                                                   @PathVariable Long physicianId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorService.createPrescription(prescription, physicianId));
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
     * Retrieves the specified doctor's prescriptions from the prescription API
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with the list of the doctor's prescriptions
     */
    @GetMapping(value = "/physician/{physicianId}/myPrescriptions")
    public ResponseEntity<Flux<PrescriptionDTO>> getPrescriptions(@PathVariable Long physicianId) {
        return ResponseEntity.ok(doctorService.getPrescriptions(physicianId));
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
     * Updates a prescription with the corresponding ID and sends it to the prescription API
     * @param prescriptionDTO - the UpdatePrescriptionDTO payload containing the updated information
     * @param prescriptionId - the ID of the prescription
     * @return - HTTP status response with the updated information
     */
    @PatchMapping(value = "/physician/updatePrescriptionInfo/{prescriptionId}",
                    consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UpdatePrescriptionDTO>> updatePrescriptionInfo(
            @RequestBody UpdatePrescriptionDTO prescriptionDTO, @PathVariable Long prescriptionId) {
        return ResponseEntity.ok(doctorService.updatePrescriptionInfo(prescriptionDTO, prescriptionId));
    }

    /**
     * Updates an appointment with the corresponding ID and sends it to the appointment API
     * @param appointmentDTO - the AppointmentDTO payload containing the updated information
     * @param appointmentId - the ID of the appointment
     * @return - HTTP status response with the updated information
     */
    @PatchMapping(value = "/physician/updateAppointmentInfo/{appointmentId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<AppointmentDTO>> updateAppointmentIndo(@RequestBody AppointmentDTO appointmentDTO,
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
     * Sends a DELETE request to the patient API to delete the patient with the specified ID
     * @param patientId - the ID of the patient
     * @return - the DELETE request
     */
    @DeleteMapping(value = "/physician/deletePatient/{patientId}")
    public ResponseEntity<Mono<Void>> deletePatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(doctorService.deletePatient(patientId));
    }

    /**
     * Sends a DELETE request to the prescription API to delete the prescription with the specified ID
     * @param prescriptionId - the ID of the prescription
     * @return - the DELETE request
     */
    @DeleteMapping(value = "/physician/deletePrescription/{prescriptionId}")
    public ResponseEntity<Mono<Void>> deletePrescription(@PathVariable Long prescriptionId) {
        return ResponseEntity.ok(doctorService.deletePrescription(prescriptionId));
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
