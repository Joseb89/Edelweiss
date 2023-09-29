package com.jaab.edelweiss.dto;

import com.jaab.edelweiss.model.Status;

public record PrescriptionDTO(Long id, String doctorFirstName, String doctorLastName, String prescriptionName,
                              Byte prescriptionDosage, Status prescriptionStatus) {
}
