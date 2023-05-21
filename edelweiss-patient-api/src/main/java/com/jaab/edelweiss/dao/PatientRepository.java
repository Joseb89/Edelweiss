package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    @Query("FROM Patient p where p.firstName = :firstName order by p.lastName")
    Set<Patient> getPatientByFirstName(@Param("firstName") String firstName);

    @Query("FROM Patient p where p.lastName = :lastName order by p.firstName")
    Set<Patient> getPatientsByLastName(@Param("lastName") String lastName);
}