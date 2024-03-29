package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.service.DoctorPatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This class is a controller for the endpoints that retrieve patient information from the patient API
 *
 * @author Joseph Barr
 */
@RestController
@RequestMapping(value = "/physician")
public class DoctorPatientController {

    private final DoctorPatientService doctorPatientService;

    public DoctorPatientController(DoctorPatientService doctorPatientService) {
        this.doctorPatientService = doctorPatientService;
    }

    /**
     * Retrieves a patient from the patient API based on the patient's ID
     *
     * @param patientId - the ID of the patient
     * @return - HTTP status response with the patient data
     */
    @GetMapping(value = "/getPatientById/{patientId}")
    public ResponseEntity<Mono<PatientDTO>> getPatientById(@PathVariable Long patientId) {
        return ResponseEntity.ok(doctorPatientService.getPatientById(patientId));
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's first name
     *
     * @param firstName - the first name of the patient
     * @return - HTTP status response with the list of patients
     */
    @GetMapping(value = "/getPatientsByFirstName/{firstName}")
    public ResponseEntity<Flux<PatientDTO>> getPatientsByFirstName(@PathVariable String firstName) {
        return ResponseEntity.ok(doctorPatientService.getPatientsByFirstName(firstName));
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's last name
     *
     * @param lastName - the last name of the patient
     * @return - HTTP status response with the list of patients
     */
    @GetMapping(value = "/getPatientsByLastName/{lastName}")
    public ResponseEntity<Flux<PatientDTO>> getPatientsByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(doctorPatientService.getPatientsByLastName(lastName));
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's blood type
     *
     * @param bloodType - the blood type of the patient
     * @return - HTTP status response with the list of patients
     */
    @GetMapping(value = "/getPatientsByBloodType/{bloodType}")
    public ResponseEntity<Flux<PatientDTO>> getPatientsByBloodType(@PathVariable String bloodType) {
        return ResponseEntity.ok(doctorPatientService.getPatientsByBloodType(bloodType));
    }

    /**
     * Retrieves the address of a patient with the specified ID from the patient API
     *
     * @param patientId - the ID of the patient
     * @return - HTTP status response with the patient's address
     */
    @GetMapping(value = "/getPatientAddress/{patientId}")
    public ResponseEntity<Mono<AddressDTO>> getPatientAddress(@PathVariable Long patientId) {
        return ResponseEntity.ok(doctorPatientService.getPatientAddress(patientId));
    }

    /**
     * Sends a DELETE request to the patient API to delete the patient with the specified ID
     *
     * @param patientId - the ID of the patient
     * @return - the DELETE request
     */
    @DeleteMapping(value = "/deletePatient/{patientId}")
    public ResponseEntity<Mono<String>> deletePatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(doctorPatientService.deletePatient(patientId));
    }

    /**
     * Handles exceptions from the patient API
     *
     * @param e - the Exception object from the patient API
     * @return - HTTP status response containing the error message
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<String> handlePatientNotFoundError(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }
}
