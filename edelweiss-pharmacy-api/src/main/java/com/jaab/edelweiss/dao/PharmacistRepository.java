package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Pharmacist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PharmacistRepository extends JpaRepository<Pharmacist, Long> {

    @Query("FROM Pharmacist p WHERE p.email = :email")
    Optional<Pharmacist> getPharmacistByEmail(@Param("email") String email);
}