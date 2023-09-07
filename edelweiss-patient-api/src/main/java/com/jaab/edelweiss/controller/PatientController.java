package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Saves a new patient to the patient database
     * @param patient - the Patient payload
     * @return - HTTP status response with the ID of the patient
     */
    @PostMapping(value = "/newPatient", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseEntity<PatientDTO> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED).body((patientService.createPatient(patient)));
    }

    /**
     * Retrieves a patient from the patient database based on the patient's ID
     * @param patientId - the ID of the patient
     * @return - the patient with the specified ID
     */
    @GetMapping(value = "/physician/getPatientById/{patientId}")
    public Mono<PatientDTO> getPatientById(@PathVariable Long patientId) {
        return Mono.just(patientService.getPatientById(patientId));
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's first name
     * @param firstName - the first name of the patient
     * @return - List of patients matching the criteria
     */
    @GetMapping(value = "/physician/getPatientsByFirstName/{firstName}")
    public Flux<PatientDTO> getPatientsByFirstName(@PathVariable String firstName) {
        return Flux.fromIterable(patientService.getPatientsByFirstName(firstName));
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's last name
     * @param lastName - the last name of the patient
     * @return - List of patients matching the criteria
     */
    @GetMapping(value = "/physician/getPatientsByLastName/{lastName}")
    public Flux<PatientDTO> getPatientsByLastName(@PathVariable String lastName) {
        return Flux.fromIterable(patientService.getPatientsByLastName(lastName));
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's blood type
     * @param bloodType - the blood type of the patient
     * @return - List of patients matching the criteria
     */
    @GetMapping(value = "/physician/getPatientsByBloodType/{bloodType}")
    public Flux<PatientDTO> getPatientsByBloodType(@PathVariable String bloodType) {
        return Flux.fromIterable(patientService.getPatientsByBloodType(bloodType));
    }

    /**
     * Retrieves the patient's address from the address database and sends it to the doctor API
     * @param patientId - the ID of the patient
     * @return - the AddressDTO object containing the patient's address
     */
    @GetMapping(value = "/physician/getPatientAddress/{patientId}")
    public Mono<AddressDTO> getAddress(@PathVariable Long patientId) {
        return Mono.just(patientService.getAddress(patientId));
    }

    /**
     * Updates the patient's address and merges it to the address database
     * @param patientId - the ID of the patient
     * @param fields - the Address payload containing the updated information
     * @return - HTTP status response
     */
    @PatchMapping(value = "/patient/{patientId}/updateAddress", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateAddress(@PathVariable Long patientId,
                                                    @RequestBody Map<String, Object> fields) {
        patientService.updateAddress(patientId, fields);
        return ResponseEntity.ok("Address successfully updated.");
    }

    /**
     * Updates the patient's information and merges it to the patient database
     * @param patientId - the ID of the patient
     * @param fields - the Patient payload containing the updated information
     * @return - HTTP status response
     */
    @PatchMapping(value = "/patient/{patientId}/updatePatientInfo", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updatePatientInfo(@PathVariable Long patientId,
                                                    @RequestBody Map<String, Object> fields) {
        patientService.updatePatientInfo(patientId, fields);
        return ResponseEntity.ok("User information updated successfully.");
    }

    /**
     * Deletes a patient from the patient database based on the patient's ID
     * @param patientId - the ID of the patient
     */
    @DeleteMapping(value = "/physician/deletePatient/{patientId}")
    public void deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
    }
}
