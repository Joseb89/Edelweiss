package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByEmail(String email);

    List<Patient> findByFirstNameOrderByLastName(String firstName);

    List<Patient> findByLastNameOrderByFirstName(String lastName);

    List<Patient> findByBloodType(String bloodType);
}