package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Pharmacist;
import com.jaab.edelweiss.service.PharmacistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PharmacistController {

    private final PharmacistService pharmacistService;

    @Autowired
    public PharmacistController(PharmacistService pharmacistService) {
        this.pharmacistService = pharmacistService;
    }

    @PostMapping(value = "/newPharmacist", consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Long> createPharmacist(@RequestBody Pharmacist pharmacist) {
        UserDTO newPharmacist = pharmacistService.createPharmacist(pharmacist);
        return ResponseEntity.ok(newPharmacist.getId());
    }
}
