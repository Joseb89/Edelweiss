package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    @Query("FROM Appointment a WHERE a.doctorFirstName = :firstName AND a.doctorLastName = :lastName " +
            "ORDER BY a.appointmentDate")
    List<Appointment> getAppointmentsByDoctorName(@Param("firstName") String firstName,
                                                  @Param("lastName") String lastName);
}