package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PrescriptionRepository;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PrescriptionService {

    private PrescriptionRepository prescriptionRepository;

    @Autowired
    public void setPrescriptionRepository(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * Creates a new prescription based on PrescriptionDTO object from the doctor API
     * @param prescriptionDTO - the PrescriptionDTO object from the doctor API
     * @return - prescription data
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
     * @return - Set of prescriptions
     */
    public Set<PrescriptionDTO> getPrescriptionsByDoctorName(String firstName, String lastName) {
        Set<Prescription> prescriptions = prescriptionRepository.getPrescriptionsByDoctorName(firstName, lastName);

        return prescriptions.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toSet());
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
