package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This class is a controller for the endpoints for creating and maintaining physician data
 *
 * @author Joseph Barr
 */
@RestController
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * Saves a new doctor to the doctor database
     *
     * @param doctor - the Doctor payload
     * @return - HTTP status response with the new doctor's info
     */
    @PostMapping(value = "/newPhysician", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Doctor> createPhysician(@RequestBody Doctor doctor) {
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.createDoctor(doctor));
    }

    /**
     * Updates the doctor's information and merges it to the doctor database
     *
     * @param physicianId - the ID of the doctor
     * @param fields      - the Doctor payload containing the updated information
     * @return - HTTP status response
     */
    @PatchMapping(value = "/physician/{physicianId}/updatePhysicianInfo",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateDoctorInfo(@PathVariable Long physicianId,
                                                   @RequestBody Map<String, Object> fields) {
        doctorService.updateDoctorInfo(physicianId, fields);
        return ResponseEntity.ok("User information updated successfully.");
    }

    /**
     * Deletes a doctor from the doctor database based on their id
     *
     * @param physicianId - the ID of the doctor
     */
    @DeleteMapping(value = "/physician/deleteDoctor/{physicianId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long physicianId) {
        doctorService.deleteDoctor(physicianId);
        return ResponseEntity.ok("Doctor successfully deleted.");
    }
}
