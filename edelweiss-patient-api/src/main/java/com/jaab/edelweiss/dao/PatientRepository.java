package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("FROM Patient p where p.firstName = :firstName ORDER BY p.lastName")
    List<Patient> getPatientsByFirstName(@Param("firstName") String firstName);

    @Query("FROM Patient p where p.lastName = :lastName ORDER BY p.firstName")
    List<Patient> getPatientsByLastName(@Param("lastName") String lastName);

    @Query("FROM Patient p where p.bloodType = :bloodType")
    List<Patient> getPatientsByBloodType(@Param("bloodType") String bloodType);
}