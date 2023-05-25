package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PrescriptionRepository;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrescriptionService {

    private PrescriptionRepository prescriptionRepository;

    @Autowired
    public void setPrescriptionRepository(PrescriptionRepository prescriptionRepository) {
        this.prescriptionRepository = prescriptionRepository;
    }

    /**
     * Creates a new prescription based on prescriptionDTO from doctor API
     * @param prescriptionDTO - the prescriptionDTO from the doctor API
     * @return - prescription data
     */
    public Prescription createPrescription(PrescriptionDTO prescriptionDTO) {
        Prescription prescription = new Prescription();
        BeanUtils.copyProperties(prescriptionDTO, prescription);
        prescription.setPrescriptionStatus(Status.PENDING);
        prescriptionRepository.save(prescription);
        return prescription;
    }
}
