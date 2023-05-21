package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/physician")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping(value = "/newPhysician", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPhysician(@RequestBody Doctor doctor) {
        UserDTO newDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.ok(newDoctor.getId());
    }

    @PostMapping(value = "/{physicianId}/newPrescription", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPrescription(@RequestBody Prescription prescription,
                                                   @PathVariable Long physicianId) {
        Prescription newPrescription = doctorService.createPrescription(prescription, physicianId);
        return ResponseEntity.ok(newPrescription.getId());
    }

    @GetMapping(value = "/getPatient/{patientId}")
    public ResponseEntity<Mono<PatientDTO>> getPatientDataById(@PathVariable Long patientId) {
        Mono<PatientDTO> patientData = doctorService.getPatientData(patientId);
        return ResponseEntity.ok(patientData);
    }
}
