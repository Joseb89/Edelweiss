package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient")
public class PatientController {

    private final PatientService patientService;

    @Autowired
    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PostMapping(value = "/newPatient", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPatient(@RequestBody Patient patient) {
        UserDTO newPatient = patientService.createPatient(patient);
        return ResponseEntity.ok(newPatient.getId());
    }
}
