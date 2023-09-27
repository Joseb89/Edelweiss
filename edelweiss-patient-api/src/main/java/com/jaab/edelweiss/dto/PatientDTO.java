package com.jaab.edelweiss.dto;

import com.jaab.edelweiss.model.Patient;

public record PatientDTO(Long id, String firstName, String lastName, String email,
                         Long phoneNumber, String primaryDoctor, String bloodType) {

    public PatientDTO(Patient patient) {
        this(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getEmail(),
                patient.getPhoneNumber(), patient.getPrimaryDoctor(), patient.getBloodType());
    }
}