package com.jaab.edelweiss.dto;

import com.jaab.edelweiss.model.Status;
public class PrescriptionStatusDTO {

    private Status prescriptionStatus;

    public Status getPrescriptionStatus() {
        return prescriptionStatus;
    }

    public void setPrescriptionStatus(Status prescriptionStatus) {
        this.prescriptionStatus = prescriptionStatus;
    }
}
