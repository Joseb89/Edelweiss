package com.jaab.edelweiss.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "doctors")
@Getter
@Setter
public class Doctor {

    @Id
    @Column(name = "doctor_id", nullable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "doctor_email", nullable = false, length = 30)
    private String email;

    @Column(name = "doctor_password", nullable = false, length = 100)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private Long phoneNumber;

    @Column(name = "practice", nullable = false, length = 25)
    private String practice;
}
