package com.jaab.edelweiss.dto;

import com.jaab.edelweiss.model.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionDTO {

    private Long id;
    private String doctorFirstName;
    private String doctorLastName;
    private String prescriptionName;
    private Byte prescriptionDosage;
    private Status prescriptionStatus;
}
