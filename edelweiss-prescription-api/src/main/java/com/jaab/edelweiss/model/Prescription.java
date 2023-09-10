package com.jaab.edelweiss.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "prescriptions")
@Getter
@Setter
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "prescription_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "doctor_first_name", nullable = false, updatable = false, length = 20)
    private String doctorFirstName;

    @Column(name = "doctor_last_name", nullable = false, updatable = false, length = 30)
    private String doctorLastName;

    @Column(name = "prescription_name", nullable = false, length = 20)
    private String prescriptionName;

    @Column(name = "prescription_dosage", nullable = false)
    private Byte prescriptionDosage;

    @Column(name = "prescription_status", nullable = false, length = 8)
    @Enumerated(EnumType.STRING)
    private Status prescriptionStatus;
}
