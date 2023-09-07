package com.jaab.edelweiss.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    @Id
    @Column(name = "patient_id", nullable = false, updatable = false, unique = true)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "patient_id")
    @MapsId
    private Patient patient;

    @Column(name = "street_address", nullable = false, length = 40)
    private String streetAddress;

    @Column(name = "city", nullable = false, length = 20)
    private String city;

    @Column(name = "state", nullable = false, length = 2)
    private String state;

    @Column(name = "zipcode", nullable = false)
    private Integer zipcode;
}
