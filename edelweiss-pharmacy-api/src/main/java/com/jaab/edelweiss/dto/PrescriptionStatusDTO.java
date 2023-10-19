package com.jaab.edelweiss.dto;

import com.jaab.edelweiss.exception.PrescriptionStatusException;
import com.jaab.edelweiss.model.Status;

public record PrescriptionStatusDTO(Status prescriptionStatus) {

    public PrescriptionStatusDTO {
        if (prescriptionStatus == Status.PENDING)
            throw new PrescriptionStatusException("Prescription must be approved or denied.");
    }
}
