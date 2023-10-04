package com.jaab.edelweiss.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "pharmacists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pharmacist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pharmacist_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @Column(name = "first_name", nullable = false, updatable = false, length = 20)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 30)
    private String lastName;

    @Column(name = "pharmacist_email", nullable = false, unique = true, length = 30)
    private String email;

    @Column(name = "pharmacist_password", nullable = false, length = 100)
    private String password;

    @Column(name = "role", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private Role role;
}
