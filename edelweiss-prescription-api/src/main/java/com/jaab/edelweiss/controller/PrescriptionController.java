package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.model.Status;
import com.jaab.edelweiss.service.PrescriptionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PrescriptionController {

    private final PrescriptionService prescriptionService;

    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    /**
     * Saves a new prescription to the prescription database based on a PrescriptionDTO payload
     * from the doctor API
     *
     * @param prescriptionDTO - the PrescriptionDTO payload from the doctor API
     * @return - the new prescription
     */
    @PostMapping(value = "/physician/newPrescription")
    public PrescriptionDTO createPrescription(@RequestBody PrescriptionDTO prescriptionDTO) {
        return prescriptionService.createPrescription(prescriptionDTO);
    }

    /**
     * Retrieves a list of prescriptions from the prescription database based on the doctor's name and sends it
     * to the doctor API
     *
     * @param firstName - the first name of the doctor
     * @param lastName  - the last name of the doctor
     * @return - the list of the doctor's prescriptions
     */
    @GetMapping(value = "/physician/myPrescriptions/{firstName}/{lastName}")
    public List<PrescriptionDTO> getPrescriptionsByDoctorName(@PathVariable String firstName,
                                                              @PathVariable String lastName) {
        return prescriptionService.getPrescriptionsByDoctorName(firstName, lastName);
    }

    /**
     * Retrieves a list of prescriptions with the PENDING status and sends it to the pharmacy API
     *
     * @return - the list of PENDING prescriptions
     */
    @GetMapping(value = "/pharmacy/getPendingPrescriptions")
    public List<PrescriptionDTO> getPendingPrescriptions() {
        return prescriptionService.getPrescriptionsByPrescriptionStatus(Status.PENDING);
    }

    /**
     * Updates the prescription with the specified ID and merges it to the prescription database
     *
     * @param prescriptionDTO - the UpdatePrescriptionDTO payload from the doctor API
     * @param prescriptionId  - the ID of the prescription
     * @return - the updated prescription
     */
    @PatchMapping(value = "/physician/updatePrescriptionInfo/{prescriptionId}")
    public PrescriptionDTO updatePrescriptionInfo(@RequestBody UpdatePrescriptionDTO prescriptionDTO,
                                                        @PathVariable Long prescriptionId) {
        return prescriptionService.updatePrescriptionInfo(prescriptionDTO, prescriptionId);
    }

    /**
     * Approves or denies a prescription based on a PrescriptionStatusDTO payload from the pharmacy API
     * and merges it to the prescription database
     *
     * @param status         - the PrescriptionStatusDTO payload containing the new status
     * @param prescriptionId - the ID of the prescription
     * @return - the PrescriptionDTO object containing the updated status
     */
    @PatchMapping(value = "/pharmacy/approvePrescription/{prescriptionId}")
    public PrescriptionDTO approvePrescription(@RequestBody PrescriptionStatusDTO status,
                                                     @PathVariable Long prescriptionId) {
        return prescriptionService.approvePrescription(status, prescriptionId);
    }

    /**
     * Deletes a prescription from the prescription database based on their ID
     *
     * @param prescriptionId - the ID of the prescription
     */
    @DeleteMapping(value = "/physician/deletePrescription/{prescriptionId}")
    public String deletePrescription(@PathVariable Long prescriptionId) {
        prescriptionService.deletePrescription(prescriptionId);

        return "Prescription successfully deleted.";
    }
}
