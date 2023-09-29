package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.model.Pharmacist;
import com.jaab.edelweiss.service.PharmacistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
public class PharmacistController {

    private final PharmacistService pharmacistService;

    public PharmacistController(PharmacistService pharmacistService) {
        this.pharmacistService = pharmacistService;
    }

    /**
     * Saves a new pharmacist to the pharmacist database
     *
     * @param pharmacist - the Pharmacist payload
     * @return - the new Pharmacist
     */
    @PostMapping(value = "/newPharmacist", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Pharmacist createPharmacist(@RequestBody Pharmacist pharmacist) {
        return pharmacistService.createPharmacist(pharmacist);
    }

    /**
     * Pharmacy home page; displays all pending prescriptions
     *
     * @return - HTTP status response containing all pending prescriptions
     */
    @GetMapping(value = "/pharmacy/home")
    public ResponseEntity<Flux<PrescriptionDTO>> getPendingPrescriptions() {
        return ResponseEntity.ok(pharmacistService.getPendingPrescriptions());
    }

    /**
     * Updates the pharmacist's information and merges it to the pharmacist database
     *
     * @param pharmacistId - the ID of the pharmacist
     * @param fields       - the Pharmacist payload
     * @return - the updated pharmacist
     */
    @PatchMapping(value = "/pharmacy/{pharmacistId}/updatePharmacistInfo",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Pharmacist updatePharmacistInfo(@PathVariable Long pharmacistId,
                                           @RequestBody Map<String, Object> fields) {
        return pharmacistService.updateUserInfo(pharmacistId, fields);
    }

    /**
     * Sets an APPROVED or DENIED status for a PrescriptionStatusDTO object and sends it to the prescription API
     *
     * @param status         - the PrescriptionStatusDTO payload containing the new status
     * @param prescriptionId - the ID of the prescription
     * @return - HTTP status containing the updated prescription status
     */
    @PatchMapping(value = "/pharmacy/approvePrescription/{prescriptionId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<PrescriptionStatusDTO>> approvePrescription(@RequestBody PrescriptionStatusDTO status,
                                                                           @PathVariable Long prescriptionId) {
        return ResponseEntity.ok(pharmacistService.approvePrescription(status, prescriptionId));
    }

    /**
     * Deletes a pharmacist from the pharmacist database and sends a DELETE request to the user API to delete
     * the user with the corresponding ID
     *
     * @param pharmacistId - the ID of the pharmacist
     * @return - HTTP response
     */
    @DeleteMapping(value = "/pharmacy/deletePharmacist/{pharmacistId}")
    public ResponseEntity<String> deletePharmacist(@PathVariable Long pharmacistId) {
        pharmacistService.deletePharmacist(pharmacistId);
        return ResponseEntity.ok("Pharmacist successfully deleted.");
    }
}
