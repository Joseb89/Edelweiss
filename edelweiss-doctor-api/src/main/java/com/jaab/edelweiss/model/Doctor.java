package com.jaab.edelweiss.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "doctor_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "first_name", nullable = false, updatable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "doctor_email", nullable = false, unique = true, length = 30)
    private String email;

    @Column(name = "doctor_password", nullable = false, length = 100)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private Long phoneNumber;

    @Column(name = "practice", nullable = false, updatable = false, length = 25)
    private String practice;

    @Column(name = "role", nullable = false, updatable = false, length = 9)
    @Enumerated(EnumType.STRING)
    private Role role;
}
