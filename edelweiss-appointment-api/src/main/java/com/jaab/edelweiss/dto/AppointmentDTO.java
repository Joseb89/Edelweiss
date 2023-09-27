package com.jaab.edelweiss.dto;

import com.jaab.edelweiss.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentDTO(Long id, String doctorFirstName, String doctorLastName, String patientFirstName,
                             String patientLastName, LocalDate appointmentDate, LocalTime appointmentTime) {

    public AppointmentDTO(Appointment appointment) {
        this(appointment.getId(), appointment.getDoctorFirstName(), appointment.getDoctorLastName(),
                appointment.getPatientFirstName(), appointment.getPatientLastName(),
                appointment.getAppointmentDate(), appointment.getAppointmentTime());
    }

}
