package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

/**
 * This class is a controller for the endpoints for creating and maintaining physician data
 *
 * @author Joseph Barr
 */
@RestController
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * Saves a new doctor to the doctor database and sends the data to the user API
     * @param doctor - the Doctor payload
     * @return - HTTP status response with the ID of the doctor
     */
    @PostMapping(value = "/newPhysician", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPhysician(@RequestBody Doctor doctor) {
        UserDTO newDoctor = doctorService.createDoctor(doctor);
        return ResponseEntity.status(HttpStatus.CREATED).body(newDoctor.getId());
    }

    /**
     * Updates the doctor's information and merges it to the doctor database
     * @param doctor - the Doctor payload containing the updated information
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with the updated information
     */
    @PatchMapping(value = "/physician/{physicianId}/updatePhysicianInfo",
                    consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UserDTO>> updateDoctorInfo(@RequestBody Doctor doctor,
                                                          @PathVariable Long physicianId) {
        return ResponseEntity.ok((doctorService.updateUserInfo(doctor, physicianId)));
    }

    /**
     * Deletes a doctor from the doctor database and sends a DELETE request to the user API to delete the user
     * with the corresponding ID
     * @param physicianId - the ID of the doctor
     * @return - the DELETE request
     */
    @DeleteMapping(value = "/physician/deleteDoctor/{physicianId}")
    public ResponseEntity<Mono<Void>> deleteDoctor(@PathVariable Long physicianId) {
        return ResponseEntity.ok(doctorService.deleteUser(physicianId));
    }
}
