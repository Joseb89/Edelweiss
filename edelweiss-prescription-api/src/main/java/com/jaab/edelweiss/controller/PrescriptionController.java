package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;
import com.jaab.edelweiss.service.PrescriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    /**
     * Saves a new prescription to the prescription database based on a PrescriptionDTO from the doctor API
     * @param prescriptionDTO - PrescriptionDTO object from the doctor API
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
    public List<PrescriptionDTO> getPrescriptionsByDoctorName(@PathVariable String firstName,
                                                              @PathVariable String lastName) {
        return prescriptionService.getPrescriptionsByDoctorName(firstName, lastName);
    }

    /**
     * Retrieves a list of prescriptions with the PENDING status and sends it to the pharmacy API
     * @return - the list of PENDING prescriptions
     */
    @GetMapping(value = "/pharmacy/getPendingPrescriptions")
    public List<PrescriptionDTO> getPendingPrescriptions() {
        return prescriptionService.getPrescriptionsByPrescriptionStatus(Status.PENDING);
    }

    /**
     * Updates the prescription with the corresponding ID and merges it to the prescription database
     * @param prescriptionDTO - the PrescriptionDTO payload from the doctor API
     * @param prescriptionId - the ID of the prescription
     * @return - the updated prescription
     */
    @PatchMapping(value = "/physician/updatePrescriptionInfo/{prescriptionId}",
                    consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PrescriptionDTO updatePrescriptionInfo(@RequestBody UpdatePrescriptionDTO prescriptionDTO,
                                                        @PathVariable Long prescriptionId) {
        return prescriptionService.updatePrescriptionInfo(prescriptionDTO, prescriptionId);
    }

    /**
     * Approves or denies a prescription based on a PrescriptionStatusDTO payload from the pharmacy API
     * and merges it to the prescription database
     * @param status - the PrescriptionStatusDTO payload containing the new status
     * @param prescriptionId - the ID of the prescription
     * @return - the PrescriptionDTO object containing the updated status
     */
    @PatchMapping(value = "/pharmacy/approvePrescription/{prescriptionId}",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public PrescriptionDTO approvePrescription(@RequestBody PrescriptionStatusDTO status,
                                                               @PathVariable Long prescriptionId) {
        return prescriptionService.approvePrescription(status, prescriptionId);
    }
}
