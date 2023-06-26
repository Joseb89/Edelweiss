package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    /**
     * Saves a new prescription to the prescription database based on prescriptionDTO from doctor API
     * @param prescriptionDTO - PrescriptionDTO object from doctor API
     * @return - the new prescription
     */
    @PostMapping(value = "/physician/newPrescription", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Prescription createPrescription(@RequestBody PrescriptionDTO prescriptionDTO) {
        return prescriptionService.createPrescription(prescriptionDTO);
    }

    /**
     * Retrieves a list of prescriptions from the prescription database based on the doctor's name and sends it
     * to the doctor API
     * @param firstName - the first name of the doctor
     * @param lastName - the last name of the doctor
     * @return - the list of the doctor's prescriptions
     */
    @GetMapping(value = "/physician/myPrescriptions/{firstName}/{lastName}")
    public Set<PrescriptionDTO> getPrescriptionsByDoctorName(@PathVariable String firstName,
                                                             @PathVariable String lastName) {
        return prescriptionService.getPrescriptionsByDoctorName(firstName, lastName);
    }
}
