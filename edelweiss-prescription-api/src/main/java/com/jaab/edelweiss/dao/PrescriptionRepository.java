package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByDoctorFirstNameAndDoctorLastName(String firstName, String lastName);

    List<Prescription> findByPrescriptionStatus(Status status);
}