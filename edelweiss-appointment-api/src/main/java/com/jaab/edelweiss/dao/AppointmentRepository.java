package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    List<Appointment> findByDoctorFirstNameAndDoctorLastNameOrderByAppointmentDate(String firstName, String lastName);

}