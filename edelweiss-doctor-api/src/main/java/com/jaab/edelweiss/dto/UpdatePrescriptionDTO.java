package com.jaab.edelweiss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePrescriptionDTO {

    private Long id;
    private String prescriptionName;
    private Byte prescriptionDosage;
}
