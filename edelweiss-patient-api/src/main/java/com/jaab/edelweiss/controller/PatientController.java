package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
