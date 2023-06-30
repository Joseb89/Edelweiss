package com.jaab.edelweiss.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "patients")
@Getter
@Setter
public class Patient {

    @Id
    @Column(name = "patient_id", nullable = false, updatable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, updatable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "patient_email", nullable = false, length = 30)
    private String email;

    @Column(name = "patient_password", nullable = false, length = 100)
    private String password;

    @Column(name = "phone_number", nullable = false)
    private Long phoneNumber;

    @Column(name = "street_address", length = 40)
    private String streetAddress;

    @Column(name = "city", length = 20)
    private String city;

    @Column(name = "state", length = 2)
    private String state;

    @Column(name = "zipcode")
    private Integer zipcode;

    @Column(name = "primary_doctor", length = 50)
    private String primaryDoctor;

    @Column(name = "blood_type", nullable = false, updatable = false, length = 3)
    private String bloodType;
}
