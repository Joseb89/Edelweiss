package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    @Query("FROM Doctor d WHERE d.id = :id")
    Optional<Doctor> getDoctorById(@Param("id") Long id);
}