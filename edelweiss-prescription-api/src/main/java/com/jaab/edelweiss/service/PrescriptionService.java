package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PrescriptionRepository;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.exception.PrescriptionNotFoundException;
import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PrescriptionService {

    private final PrescriptionRepository prescriptionRepository;

    public PrescriptionService(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * Creates a new prescription based on a PrescriptionDTO object from the doctor API
     *
     * @param prescriptionDTO - the PrescriptionDTO object from the doctor API
     * @return - the prescription data
     */
    public PrescriptionDTO createPrescription(PrescriptionDTO prescriptionDTO) {
        Prescription prescription = new Prescription();
        BeanUtils.copyProperties(prescriptionDTO, prescription);
        prescription.setPrescriptionStatus(Status.PENDING);

        prescriptionRepository.save(prescription);

        return new PrescriptionDTO(prescription);
    }

    /**
     * Retrieves a list of prescriptions from the prescription database based on the doctor's name
     *
     * @param firstName - the first name of the doctor
     * @param lastName  - the last name of the doctor
     * @return - the list of prescriptions matching the criteria
     */
    public List<PrescriptionDTO> getPrescriptionsByDoctorName(String firstName, String lastName) {
        return getPrescriptions(prescriptionRepository.getPrescriptionsByDoctorName(firstName, lastName));
    }

    /**
     * Retrieves a list of prescriptions based on their current status
     *
     * @param status - the status of the prescription
     * @return - the list of prescriptions with the specified status
     */
    public List<PrescriptionDTO> getPrescriptionsByPrescriptionStatus(Status status) {
        return getPrescriptions(prescriptionRepository.getPrescriptionsByPrescriptionStatus(status));
    }

    /**
     * Updates the prescription with the specified ID and merges it to the prescription database
     *
     * @param prescriptionDTO - the UpdatePrescriptionDTO object from the doctor API
     * @param prescriptionId  - the ID of the prescription
     * @return - the updated prescription
     * @throws PrescriptionNotFoundException if the prescription with the specified ID is not found
     */
    public PrescriptionDTO updatePrescriptionInfo(UpdatePrescriptionDTO prescriptionDTO,
                                                  Long prescriptionId) {
        Prescription prescription = getPrescriptionById(prescriptionId);

        if (prescriptionDTO.prescriptionName() != null)
            prescription.setPrescriptionName(prescriptionDTO.prescriptionName());

        if (prescriptionDTO.prescriptionDosage() != null)
            prescription.setPrescriptionDosage(prescriptionDTO.prescriptionDosage());

        prescriptionRepository.save(prescription);

        return new PrescriptionDTO(prescription);
    }

    /**
     * Sets an APPROVED or DENIED status to a PENDING prescription based on a PrescriptionStatusDTO object
     * from the pharmacy API and merges it to the prescription database
     *
     * @param status         - the PrescriptionStatusDTO object containing the new status
     * @param prescriptionId - the ID of the prescription
     * @return - the PrescriptionDTO object containing the updated status
     * @throws PrescriptionNotFoundException if the prescription with the specified ID is not found
     */
    public PrescriptionDTO approvePrescription(PrescriptionStatusDTO status, Long prescriptionId)
            throws PrescriptionNotFoundException {
        Prescription prescription = getPrescriptionById(prescriptionId);
        prescription.setPrescriptionStatus(status.prescriptionStatus());

        prescriptionRepository.save(prescription);

        return new PrescriptionDTO(prescription);
    }

    /**
     * Deletes a prescription from the prescription database based on their ID
     *
     * @param prescriptionId - the ID of the prescription
     * @throws PrescriptionNotFoundException if the prescription with the specified ID is not found
     */
    public void deletePrescription(Long prescriptionId) throws PrescriptionNotFoundException {
        Prescription prescription = getPrescriptionById(prescriptionId);

        prescriptionRepository.deleteById(prescription.getId());
    }

    /**
     * Retrieves a prescription from the prescription database based on their ID
     *
     * @param prescriptionId - the ID of the prescription
     * @return - the prescription if available
     * @throws PrescriptionNotFoundException if the prescription with the specified ID is not found
     */
    private Prescription getPrescriptionById(Long prescriptionId) throws PrescriptionNotFoundException {
        return prescriptionRepository.findById(prescriptionId)
                .orElseThrow(()-> new PrescriptionNotFoundException("No prescription with the specified ID found."));
    }

    private List<PrescriptionDTO> getPrescriptions(List<Prescription> prescriptionList) {
        return prescriptionList.stream()
                .map(PrescriptionDTO::new)
                .toList();
    }
}
