package com.jaab.edelweiss.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "appointments")
@Getter
@Setter
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointment_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "doctor_first_name", nullable = false, updatable = false, length = 20)
    private String doctorFirstName;

    @Column(name = "doctor_last_name", nullable = false, updatable = false, length = 30)
    private String doctorLastName;

    @Column(name = "patient_first_name", nullable = false, length = 20)
    private String patientFirstName;

    @Column(name = "patient_last_name", nullable = false, length = 30)
    private String patientLastName;

    @Column(name = "appointment_date", nullable = false)
    private LocalDate appointmentDate;

    @Column(name = "appointment_time", nullable = false)
    private LocalTime appointmentTime;
}
