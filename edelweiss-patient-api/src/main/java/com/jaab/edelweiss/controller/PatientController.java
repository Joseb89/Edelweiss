package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Saves a new patient to the patient database and sends the data to the user API
     * @param patient - the Patient payload
     * @return - HTTP status response with the ID of the patient
     */
    @PostMapping(value = "/newPatient", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPatient(@RequestBody Patient patient) {
        UserDTO newPatient = patientService.createPatient(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPatient.getId());
    }

    /**
     * Retrieves a patient from the patient database based on the patient's ID
     * @param patientId - the ID of the patient
     * @return - the patient with the specified ID
     */
    @GetMapping(value = "/physician/getPatientById/{patientId}")
    public PatientDTO getPatientById(@PathVariable Long patientId) {
        return patientService.getPatientById(patientId);
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's first name
     * @param firstName - the first name of the patient
     * @return - List of patients matching the criteria
     */
    @GetMapping(value = "/physician/getPatientsByFirstName/{firstName}")
    public List<PatientDTO> getPatientsByFirstName(@PathVariable String firstName) {
        return patientService.getPatientsByFirstName(firstName);
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's last name
     * @param lastName - the last name of the patient
     * @return - List of patients matching the criteria
     */
    @GetMapping(value = "/physician/getPatientsByLastName/{lastName}")
    public List<PatientDTO> getPatientsByLastName(@PathVariable String lastName) {
        return patientService.getPatientsByLastName(lastName);
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's blood type
     * @param bloodType - the blood type of the patient
     * @return - List of patients matching the criteria
     */
    @GetMapping(value = "/physician/getPatientsByBloodType/{bloodType}")
    public List<PatientDTO> getPatientsByBloodType(@PathVariable String bloodType) {
        return patientService.getPatientsByBloodType(bloodType);
    }

    /**
     * Retrieves the patient's address from the address database and sends it to the doctor API
     * @param patientId - the ID of the patient
     * @return - the AddressDTO object containing the patient's address
     */
    @GetMapping(value = "/physician/getPatientAddress/{patientId}")
    public AddressDTO getAddress(@PathVariable Long patientId) {
        return patientService.getAddress(patientId);
    }

    /**
     * Updates the patient's address and merges it to the address database
     * @param address - the Address payload containing the updated information
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
