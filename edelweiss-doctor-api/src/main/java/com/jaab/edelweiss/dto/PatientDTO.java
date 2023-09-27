package com.jaab.edelweiss.dto;

public record PatientDTO(Long id, String firstName, String lastName, String email, Long phoneNumber,
                         String primaryDoctor, String bloodType) {
}
