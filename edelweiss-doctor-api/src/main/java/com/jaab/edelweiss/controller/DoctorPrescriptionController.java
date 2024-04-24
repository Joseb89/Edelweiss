package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.exception.PrescriptionException;
import com.jaab.edelweiss.service.DoctorPrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class is a controller for the endpoints that create and maintain prescription data
 *
 * @author Joseph Barr
 */
@RestController
@RequestMapping(value = "/physician")
public class DoctorPrescriptionController {

    private final DoctorPrescriptionService doctorPrescriptionService;

    public DoctorPrescriptionController(DoctorPrescriptionService doctorPrescriptionService) {
        this.doctorPrescriptionService = doctorPrescriptionService;
    }

    /**
     * Sends a PrescriptionDTO payload to the prescription API
     *
     * @param prescription - the PrescriptionDTO payload
     * @return - HTTP status response with the new prescription
     */
    @PostMapping(value = "/newPrescription")
    public ResponseEntity<PrescriptionDTO> createPrescription(@RequestBody PrescriptionDTO prescription) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorPrescriptionService.createPrescription(prescription));
    }

    /**
     * Retrieves the specified doctor's prescriptions from the prescription API
     *
     * @return - HTTP status response with the list of the doctor's prescriptions
     */
    @GetMapping(value = "/myPrescriptions")
    public ResponseEntity<List<PrescriptionDTO>> getPrescriptions() {
        return ResponseEntity.ok(doctorPrescriptionService.getPrescriptions());
    }

    /**
     * Updates a prescription with the specified ID and sends it to the prescription API
     *
     * @param prescriptionDTO - the UpdatePrescriptionDTO payload containing the updated information
     * @param prescriptionId  - the ID of the prescription
     * @return - HTTP status response with the updated information
     */
    @PatchMapping(value = "/updatePrescriptionInfo/{prescriptionId}")
    public ResponseEntity<UpdatePrescriptionDTO> updatePrescriptionInfo(
            @RequestBody UpdatePrescriptionDTO prescriptionDTO, @PathVariable Long prescriptionId) {
        return ResponseEntity.ok(doctorPrescriptionService.updatePrescriptionInfo(prescriptionDTO, prescriptionId));
    }

    /**
     * Sends a DELETE request to the prescription API to delete the prescription with the specified ID
     *
     * @param prescriptionId - the ID of the prescription
     * @return - the DELETE request
     */
    @DeleteMapping(value = "/deletePrescription/{prescriptionId}")
    public ResponseEntity<String> deletePrescription(@PathVariable Long prescriptionId) {
        return ResponseEntity.ok(doctorPrescriptionService.deletePrescription(prescriptionId));
    }

    /**
     * Handles PrescriptionException errors when creating and updating prescription data
     *
     * @param e - the PrescriptionException object
     * @return - HTTP status response containing the error message
     */
    @ExceptionHandler(PrescriptionException.class)
    private ResponseEntity<String> handlePrescriptionError(PrescriptionException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
    }
}
