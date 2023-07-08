package com.jaab.edelweiss.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pharmacists")
@Getter
@Setter
public class Pharmacist {

    @Id
    @Column(name = "pharmacist_id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "first_name", nullable = false, updatable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "pharmacist_email", nullable = false, length = 30)
    private String email;

    @Column(name = "pharmacist_password", nullable = false, length = 100)
    private String password;
}
