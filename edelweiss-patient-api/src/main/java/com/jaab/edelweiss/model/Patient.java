package com.jaab.edelweiss.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "patient_id", nullable = false, updatable = false, unique = true)
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

    @Column(name = "role", nullable = false, updatable = false, length = 7)
    @Enumerated(EnumType.STRING)
    private Role role;
}
