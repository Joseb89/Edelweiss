package com.jaab.edelweiss.dto;

import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;


public record PrescriptionDTO(Long id, String doctorFirstName, String doctorLastName, String prescriptionName,
                              Byte prescriptionDosage, Status prescriptionStatus) {

    public PrescriptionDTO(Prescription prescription) {
        this(prescription.getId(), prescription.getDoctorFirstName(), prescription.getDoctorLastName(),
                prescription.getPrescriptionName(), prescription.getPrescriptionDosage(),
                prescription.getPrescriptionStatus());
    }
}
