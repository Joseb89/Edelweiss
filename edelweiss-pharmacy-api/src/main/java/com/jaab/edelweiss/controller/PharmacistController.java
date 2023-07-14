package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Pharmacist;
import com.jaab.edelweiss.service.PharmacistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class PharmacistController {

    private final PharmacistService pharmacistService;

    @Autowired
    public PharmacistController(PharmacistService pharmacistService) {
        this.pharmacistService = pharmacistService;
    }

    /**
     * Saves a new pharmacist to the pharmacist database and sends the data to the user API
     * @param pharmacist - the Pharmacist payload
     * @return - HTTP status response with the ID of the pharmacist
     */
    @PostMapping(value = "/newPharmacist", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPharmacist(@RequestBody Pharmacist pharmacist) {
        UserDTO newPharmacist = pharmacistService.createPharmacist(pharmacist);
        return ResponseEntity.status(HttpStatus.CREATED).body(newPharmacist.getId());
    }

    /**
     * Pharmacy home page; displays all pending prescriptions
     * @return - HTTP status response containing all pending prescriptions
     */
    @GetMapping(value = "/pharmacy/home")
    public ResponseEntity<Flux<PrescriptionDTO>> getPendingPrescriptions() {
        return ResponseEntity.ok(pharmacistService.getPendingPrescriptions());
    }

    /**
     * Updates the pharmacist's information and merges it to the pharmacist database
     * @param pharmacist - the Pharmacist payload containing the updated information
     * @param pharmacistId - the ID of the pharmacist
     * @return - HTTP status response with the updated information
     */
    @PatchMapping(value = "/pharmacy/{pharmacistId}/updatePharmacistInfo",
                    consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<UserDTO>> updatePharmacistInfo(@RequestBody Pharmacist pharmacist,
                                                              @PathVariable Long pharmacistId) {
        return ResponseEntity.ok(pharmacistService.updateUserInfo(pharmacist, pharmacistId));
    }

    /**
     * Sets an APPROVED or DENIED status for a PrescriptionStatusDTO object and sends it to the prescription API
     * @param status - the PrescriptionStatusDTO payload containing the new status
     * @param prescriptionId - the ID of the prescription
     * @return - HTTP status containing the updated prescription status
     */
    @PatchMapping(value = "/pharmacy/approvePrescription/{prescriptionId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mono<PrescriptionStatusDTO>> approvePrescription (@RequestBody PrescriptionStatusDTO status,
                                                            @PathVariable Long prescriptionId) {
        return ResponseEntity.ok(pharmacistService.approvePrescription(status, prescriptionId));
    }
}
