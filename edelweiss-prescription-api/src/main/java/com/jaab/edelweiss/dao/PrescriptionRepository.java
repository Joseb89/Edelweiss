package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    @Query("FROM Prescription p WHERE p.doctorFirstName = :firstName AND p.doctorLastName = :lastName")
    Set<Prescription> getPrescriptionsByDoctorName(@Param("firstName") String firstName,
                                                   @Param("lastName") String lastName);

    @Query("FROM Prescription p WHERE p.prescriptionStatus = :status")
    Set<Prescription> getPrescriptionsByPrescriptionStatus(@Param("status")Status status);
}