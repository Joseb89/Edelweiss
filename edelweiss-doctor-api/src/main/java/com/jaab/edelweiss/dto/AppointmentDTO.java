package com.jaab.edelweiss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentDTO {

    private Long id;
    private String doctorFirstName;
    private String doctorLastName;
    private String patientFirstName;
    private String patientLastName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
}
