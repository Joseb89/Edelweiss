package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.service.PharmacistPrescriptionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This class is a controller for the endpoints for communicating with the prescription API
 *
 * @author Joseph Barr
 */
@RestController
@RequestMapping(value = "/pharmacy")
public class PharmacistPrescriptionController {

    private final PharmacistPrescriptionService pharmacistPrescriptionService;

    public PharmacistPrescriptionController(PharmacistPrescriptionService pharmacistPrescriptionService) {
        this.pharmacistPrescriptionService = pharmacistPrescriptionService;
    }

    /**
     * Pharmacy home page; displays all pending prescriptions
     *
     * @return - HTTP status response containing all pending prescriptions
     */
    @GetMapping(value = "/home")
    public ResponseEntity<Flux<PrescriptionDTO>> getPendingPrescriptions() {
        return ResponseEntity.ok(pharmacistPrescriptionService.getPendingPrescriptions());
    }

    /**
     * Sets an APPROVED or DENIED status for a PrescriptionStatusDTO payload and sends it to the prescription API
     *
     * @param status         - the PrescriptionStatusDTO payload containing the new status
     * @param prescriptionId - the ID of the prescription
     * @return - HTTP status response containing the updated prescription status
     */
    @PatchMapping(value = "/approvePrescription/{prescriptionId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<PrescriptionStatusDTO>> approvePrescription(@RequestBody PrescriptionStatusDTO status,
                                                                           @PathVariable Long prescriptionId) {
        return ResponseEntity.ok(pharmacistPrescriptionService.approvePrescription(status, prescriptionId));
    }
}
