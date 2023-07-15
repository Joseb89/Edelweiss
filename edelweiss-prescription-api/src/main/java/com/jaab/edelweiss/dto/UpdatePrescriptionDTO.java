package com.jaab.edelweiss.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdatePrescriptionDTO {

    private Long id;
    private String prescriptionName;
    private Byte prescriptionDosage;
}
