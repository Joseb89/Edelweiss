package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.service.DoctorPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(value = "/physician")
public class DoctorPatientController {

    private final DoctorPatientService doctorPatientService;

    @Autowired
    public DoctorPatientController(DoctorPatientService doctorPatientService) {
        this.doctorPatientService = doctorPatientService;
    }

    /**
     * Retrieves a patient from the patient API based on the patient's ID
     * @param patientId - the ID of the patient
     * @return - HTTP status response with the patient data
     */
    @GetMapping(value = "/getPatientById/{patientId}")
    public ResponseEntity<Mono<PatientDTO>> getPatientById(@PathVariable Long patientId) {
        return ResponseEntity.ok(doctorPatientService.getPatientById(patientId));
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's first name
     * @param firstName - the first name of the patient
     * @return - HTTP status response with the list of patients
     */
    @GetMapping(value = "/getPatientsByFirstName/{firstName}")
    public ResponseEntity<Flux<PatientDTO>> getPatientsByFirstName(@PathVariable String firstName) {
        return ResponseEntity.ok(doctorPatientService.getPatientsByFirstName(firstName));
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's last name
     * @param lastName - the last name of the patient
     * @return - HTTP status response with the list of patients
     */
    @GetMapping(value = "/getPatientsByLastName/{lastName}")
    public ResponseEntity<Flux<PatientDTO>> getPatientsByLastName(@PathVariable String lastName) {
        return ResponseEntity.ok(doctorPatientService.getPatientsByLastName(lastName));
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's blood type
     * @param bloodType - the blood type of the patient
     * @return - HTTP status response with the list of patients
     */
    @GetMapping(value = "/getPatientsByBloodType/{bloodType}")
    public ResponseEntity<Flux<PatientDTO>> getPatientsByBloodType(@PathVariable String bloodType) {
        return ResponseEntity.ok(doctorPatientService.getPatientsByBloodType(bloodType));
    }

    /**
     * Retrieves the address of a patient with the corresponding ID from the patient API
     * @param patientId - the ID of the patient
     * @return - HTTP status response with the patient's address
     */
    @GetMapping(value = "/getPatientAddress/{patientId}")
    public ResponseEntity<Mono<AddressDTO>> getPatientAddress(@PathVariable Long patientId) {
        return ResponseEntity.ok(doctorPatientService.getPatientAddress(patientId));
    }
}
