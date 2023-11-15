package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.model.Pharmacist;
import com.jaab.edelweiss.service.PharmacistService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This class is a controller for the endpoints that create and maintain pharmacist data
 *
 * @author Joseph Barr
 */
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
     * @return - the new pharmacist
     */
    @PostMapping(value = "/newPharmacist", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Pharmacist createPharmacist(@RequestBody Pharmacist pharmacist) {
        return pharmacistService.createPharmacist(pharmacist);
    }

    /**
     * Updates the pharmacist's information and merges it to the pharmacist database
     *
     * @param pharmacistId - the ID of the pharmacist
     * @param fields       - the payload containing the updated information
     * @return - the updated pharmacist
     */
    @PatchMapping(value = "/pharmacy/{pharmacistId}/updatePharmacistInfo",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Pharmacist updatePharmacistInfo(@PathVariable Long pharmacistId,
                                           @RequestBody Map<String, Object> fields) {
        return pharmacistService.updatePharmacistInfo(pharmacistId, fields);
    }

    /**
     * Deletes a pharmacist from the pharmacist database
     *
     * @param pharmacistId - the ID of the pharmacist
     * @return - HTTP status response
     */
    @DeleteMapping(value = "/pharmacy/deletePharmacist/{pharmacistId}")
    public ResponseEntity<String> deletePharmacist(@PathVariable Long pharmacistId) {
        pharmacistService.deletePharmacist(pharmacistId);

        return ResponseEntity.ok("Pharmacist successfully deleted.");
    }
}
