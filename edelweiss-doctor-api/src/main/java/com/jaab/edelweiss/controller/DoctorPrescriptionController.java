package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.service.DoctorPrescriptionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This class is a controller for the endpoints for creating and maintaining prescription data
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
     * Sends a prescription payload to the prescription API
     *
     * @param prescription - the PrescriptionDTO payload
     * @param physicianId  - the ID of the doctor
     * @return - HTTP status response with the prescription payload
     */
    @PostMapping(value = "/{physicianId}/newPrescription", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<PrescriptionDTO>> createPrescription(
            @RequestBody PrescriptionDTO prescription,
            @PathVariable Long physicianId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(doctorPrescriptionService.createPrescription(prescription, physicianId));
    }

    /**
     * Retrieves the specified doctor's prescriptions from the prescription API
     *
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response with the list of the doctor's prescriptions
     */
    @GetMapping(value = "/{physicianId}/myPrescriptions")
    public ResponseEntity<Flux<PrescriptionDTO>> getPrescriptions(@PathVariable Long physicianId) {
        return ResponseEntity.ok(doctorPrescriptionService.getPrescriptions(physicianId));
    }

    /**
     * Updates a prescription with the corresponding ID and sends it to the prescription API
     *
     * @param prescriptionDTO - the UpdatePrescriptionDTO payload containing the updated information
     * @param prescriptionId  - the ID of the prescription
     * @return - HTTP status response with the updated information
     */
    @PatchMapping(value = "/updatePrescriptionInfo/{prescriptionId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UpdatePrescriptionDTO>> updatePrescriptionInfo(
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
    public ResponseEntity<Mono<Void>> deletePrescription(@PathVariable Long prescriptionId) {
        return ResponseEntity.ok(doctorPrescriptionService.deletePrescription(prescriptionId));
    }
}
