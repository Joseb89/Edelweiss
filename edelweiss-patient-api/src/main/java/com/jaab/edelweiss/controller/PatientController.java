package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Set;

@RestController
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Saves a new patient to the patient database and sends data to the user API
     * @param patient - the patient payload
     * @return - HTTP status response with ID of patient
     */
    @PostMapping(value = "/newPatient", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPatient(@RequestBody Patient patient) {
        UserDTO newPatient = patientService.createPatient(patient);
        return ResponseEntity.ok(newPatient.getId());
    }

    /**
     * Retrieves a patient from the patient database based on ID
     * @param patientId - the ID of the patient
     * @return - the patient with the specified ID
     */
    @GetMapping(value = "/physician/getPatientById/{patientId}")
    public PatientDTO getPatientById(@PathVariable Long patientId) {
        return patientService.getPatientById(patientId);
    }

    /**
     * Retrieves a list of patients from the patient database based on patient's first name
     * @param firstName - the first name of the patient
     * @return - Set of patients
     */
    @GetMapping(value = "/physician/getPatientsByFirstName/{firstName}")
    public Set<PatientDTO> getPatientsByFirstName(@PathVariable String firstName) {
        return patientService.getPatientsByFirstName(firstName);
    }

    /**
     * Retrieves a list of patients from the patient database based on patient's last name
     * @param lastName - the last name of the patient
     * @return - Set of patients
     */
    @GetMapping(value = "/physician/getPatientsByLastName/{lastName}")
    public Set<PatientDTO> getPatientsByLastName(@PathVariable String lastName) {
        return patientService.getPatientsByLastName(lastName);
    }

    /**
     * Retrieves a list of patients from the patient database based on patient's blood type
     * @param bloodType - the blood type of the patient
     * @return - Set of patients
     */
    @GetMapping(value = "/physician/getPatientsByBloodType/{bloodType}")
    public Set<PatientDTO> getPatientsByBloodType(@PathVariable String bloodType) {
        return patientService.getPatientsByBloodType(bloodType);
    }

    /**
     * Retrieves the patient's address from the address database and sends it to the doctor API
     * @param patientId - the ID of the patient
     * @return - the AddressDTO object
     */
    @GetMapping(value = "/physician/getPatientAddress/{patientId}")
    public AddressDTO getAddress(@PathVariable Long patientId) {
        return patientService.getAddress(patientId);
    }

    /**
     * Updates the patient's address and merges it to the address database
     * @param address - the Address payload
     * @param patientId - the ID of the patient
     * @return - HTTP status response containing the updated address
     */
    @PatchMapping(value = "/patient/{patientId}/updateAddress", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AddressDTO> updateAddress(@RequestBody Address address, @PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.updateAddress(address, patientId));
    }

    /**
     * Updates the patient's information and merges it to the patient database
     * @param patient - the Patient payload containing the updated information
     * @param patientId - the ID of the patient
     * @return - HTTP status response with the updated information
     */
    @PatchMapping(value = "/patient/{patientId}/updatePatientInfo", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UserDTO>> updatePatientInfo(@RequestBody Patient patient,
                                                           @PathVariable Long patientId) {
        return ResponseEntity.ok(patientService.updateUserInfo(patient, patientId));
    }
}
