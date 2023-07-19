package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PrescriptionRepository;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private PrescriptionRepository prescriptionRepository;

    @Autowired
    public void setPrescriptionRepository(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * Creates a new prescription based on a PrescriptionDTO object from the doctor API
     * @param prescriptionDTO - the PrescriptionDTO object from the doctor API
     * @return - the prescription data
     */
    public Prescription createPrescription(PrescriptionDTO prescriptionDTO) {
        Prescription prescription = new Prescription();
        BeanUtils.copyProperties(prescriptionDTO, prescription);
        prescription.setPrescriptionStatus(Status.PENDING);
        prescriptionRepository.save(prescription);
        return prescription;
    }

    /**
     * Retrieves a list of prescriptions from the prescription database based on the doctor's name
     * @param firstName - the first name of the doctor
     * @param lastName - the last name of the doctor
     * @return - the List of prescriptions matching the criteria
     */
    public List<PrescriptionDTO> getPrescriptionsByDoctorName(String firstName, String lastName) {
        List<Prescription> prescriptions = prescriptionRepository.getPrescriptionsByDoctorName(firstName, lastName);

        return prescriptions.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of prescriptions based on their current status
     * @param status - the status of the prescription
     * @return - the List of prescriptions with the specified status
     */
    public List<PrescriptionDTO> getPrescriptionsByPrescriptionStatus(Status status) {
        List<Prescription> prescriptions = prescriptionRepository.getPrescriptionsByPrescriptionStatus(status);

        return prescriptions.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates the prescription with the corresponding ID and merges it to the prescription database
     * @param prescriptionDTO - the PrescriptionDTO payload from the doctor API
     * @param prescriptionId - the ID of the prescription
     * @return - the updated prescription
     */
    public PrescriptionDTO updatePrescriptionInfo(UpdatePrescriptionDTO prescriptionDTO,
                                                        Long prescriptionId) {
        Prescription prescription = prescriptionRepository.getReferenceById(prescriptionId);
        PrescriptionDTO getPrescription = new PrescriptionDTO();
        getPrescription.setId(prescription.getId());

        if (prescriptionDTO.getPrescriptionName() != null)
            prescription.setPrescriptionName(prescriptionDTO.getPrescriptionName());

        if (prescriptionDTO.getPrescriptionDosage() != null)
            prescription.setPrescriptionDosage(prescriptionDTO.getPrescriptionDosage());

        prescriptionRepository.save(prescription);

        BeanUtils.copyProperties(prescription, getPrescription);

        return getPrescription;
    }

    /**
     * Sets an APPROVED or DENIED status to a PENDING prescription based on a PrescriptionStatusDTO payload
     * from the pharmacy API and merges it to the prescription database
     * @param status - the new status of the prescription
     * @param prescriptionId - the ID of the prescription
     * @return - the PrescriptionDTO object containing the updated status
     */
    public PrescriptionDTO approvePrescription(PrescriptionStatusDTO status, Long prescriptionId) {
        Prescription prescription = prescriptionRepository.getReferenceById(prescriptionId);
        prescription.setPrescriptionStatus(status.getPrescriptionStatus());
        prescriptionRepository.save(prescription);

        PrescriptionDTO updatedPrescription = new PrescriptionDTO();
        BeanUtils.copyProperties(prescription, updatedPrescription);

        return updatedPrescription;
    }

    /**
     * Deletes a prescription from the prescription database based on their ID
     * @param prescriptionId - the ID of the prescription
     */
    public void deletePrescription(Long prescriptionId) {
        prescriptionRepository.deleteById(prescriptionId);
    }

    /**
     * Copies the values of a Prescription object into a PrescriptionDTO object
     * @param prescription - the Prescription object
     * @return - the PrescriptionDTO object
     */
    private PrescriptionDTO copyToDTO(Prescription prescription) {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO();
        BeanUtils.copyProperties(prescription, prescriptionDTO);
        return prescriptionDTO;
    }
}
