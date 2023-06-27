package com.jaab.edelweiss.dto;

import com.jaab.edelweiss.model.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PrescriptionDTO {

    private String doctorFirstName;
    private String doctorLastName;
    private String prescriptionName;
    private Byte prescriptionDosage;
    private Status prescriptionStatus;
}
