package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.service.DoctorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This class is a controller for the endpoints that create and maintain physician data
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
     * @return - the new doctor
     */
    @PostMapping(value = "/newPhysician", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.createDoctor(doctor);
    }

    /**
     * Updates the doctor's information and merges it to the doctor database
     *
     * @param fields - the payload containing the updated information
     * @return - HTTP status response with the updated doctor
     */
    @PatchMapping(value = "/physician/updatePhysicianInfo",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Doctor> updateDoctorInfo(@RequestBody Map<String, Object> fields) {
        return ResponseEntity.ok(doctorService.updateDoctorInfo(fields));
    }

    /**
     * Deletes a doctor from the doctor database based on their ID
     *
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response
     */
    @DeleteMapping(value = "/physician/deleteDoctor/{physicianId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long physicianId) {
        doctorService.deleteDoctor(physicianId);

        return ResponseEntity.ok("Doctor successfully deleted.");
    }
}
