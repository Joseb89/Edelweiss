package com.jaab.edelweiss.dto;

import com.jaab.edelweiss.exception.PrescriptionException;

public record UpdatePrescriptionDTO(Long id, String prescriptionName, Byte prescriptionDosage) {

    public UpdatePrescriptionDTO {
        if (prescriptionName != null && prescriptionName.isEmpty())
            throw new PrescriptionException("Please specify prescription name.");

        if (prescriptionDosage != null && prescriptionDosage < 1)
            throw new PrescriptionException("Prescription dosage must be between 1cc and 127cc.");
    }
}
