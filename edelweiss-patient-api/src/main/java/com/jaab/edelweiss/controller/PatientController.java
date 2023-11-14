package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.service.PatientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This class is a controller for the endpoints that create and maintain patient data
 *
 * @author Jospeh Barr
 */
@RestController
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    /**
     * Saves a new patient to the patient database
     *
     * @param patient - the Patient payload
     * @return - the new patient
     */
    @PostMapping(value = "/newPatient", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public PatientDTO createPatient(@RequestBody Patient patient) {
        return patientService.createPatient(patient);
    }

    /**
     * Updates the patient's address and merges it to the address database
     *
     * @param patientId - the ID of the patient
     * @param fields    - the payload containing the updated information
     * @return - the updated address
     */
    @PatchMapping(value = "/patient/{patientId}/updateAddress", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Address updateAddress(@PathVariable Long patientId,
                                 @RequestBody Map<String, Object> fields) {
        return patientService.updateAddress(patientId, fields);
    }

    /**
     * Updates the patient's information and merges it to the patient database
     *
     * @param patientId - the ID of the patient
     * @param fields    - the payload containing the updated information
     * @return - the updated patient information
     */
    @PatchMapping(value = "/patient/{patientId}/updatePatientInfo", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Patient updatePatientInfo(@PathVariable Long patientId,
                                     @RequestBody Map<String, Object> fields) {
        return patientService.updatePatientInfo(patientId, fields);
    }
}
