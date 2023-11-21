package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
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
     * @param fields - the payload containing the updated information
     * @return - the updated address
     */
    @PatchMapping(value = "/patient/updateAddress", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public AddressDTO updateAddress(@RequestBody Map<String, Object> fields) {
        return patientService.updateAddress(fields);
    }

    /**
     * Updates the patient's information and merges it to the patient database
     *
     * @param fields - the payload containing the updated information
     * @return - the updated patient information
     */
    @PatchMapping(value = "/patient/updatePatientInfo", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientDTO updatePatientInfo(@RequestBody Map<String, Object> fields) {
        return patientService.updatePatientInfo(fields);
    }
}
