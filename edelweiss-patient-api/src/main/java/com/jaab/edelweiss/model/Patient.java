package com.jaab.edelweiss.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patients")
@Getter
@Setter
public class Patient {

    @Id
    @Column(name = "patient_id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, updatable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "patient_email", nullable = false, length = 30)
    private String email;

    @Column(name = "patient_password", nullable = false, length = 100)
    private String password;

    @OneToOne(mappedBy = "patient", optional = false, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    private Address address;

    @Column(name = "phone_number", nullable = false)
    private Long phoneNumber;

    @Column(name = "primary_doctor", length = 50)
    private String primaryDoctor;

    @Column(name = "blood_type", nullable = false, updatable = false, length = 3)
    private String bloodType;
}
