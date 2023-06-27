package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Pharmacist;
import com.jaab.edelweiss.service.PharmacistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class PharmacistController {

    private final PharmacistService pharmacistService;

    @Autowired
    public PharmacistController(PharmacistService pharmacistService) {
        this.pharmacistService = pharmacistService;
    }

    /**
     * Saves a new pharmacist to the pharmacist database and sends data to user API
     * @param pharmacist - the pharmacist payload
     * @return - HTTP status response with ID of pharmacist
     */
    @PostMapping(value = "/newPharmacist", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPharmacist(@RequestBody Pharmacist pharmacist) {
        UserDTO newPharmacist = pharmacistService.createPharmacist(pharmacist);
        return ResponseEntity.ok(newPharmacist.getId());
    }

    @GetMapping(value = "/pharmacy/home")
    public ResponseEntity<Flux<PrescriptionDTO>> getPendingPrescriptions() {
        return ResponseEntity.ok(pharmacistService.getPendingPrescriptions());
    }
}
