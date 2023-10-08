package com.jaab.edelweiss.utils;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.model.Appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class TestUtils {

    public static String doctorFirstName = "Rinoa";

    public static String doctorLastName = "Heartily";

    public static final int YEAR = (LocalDate.now().getYear()) + 1;

    public static Appointment mayAppointment = new Appointment(1L, doctorFirstName, doctorLastName,
            "Squall", "Leonheart", LocalDate.of(YEAR, 5, 5),
            LocalTime.of(10, 0));

    public static Appointment juneAppointment = new Appointment(2L, doctorFirstName, doctorLastName,
            "Zidaine", "Tribal", LocalDate.of(YEAR, 6, 6),
            LocalTime.of(10, 30));

    public static Appointment julyAppointment = new Appointment(3L, "Doctor", "Cid",
            "Vayne", "Solidor", LocalDate.of(YEAR, 7, 9),
            LocalTime.of(8, 0));

    public static List<Appointment> getAppointmentsByDoctorName() {
        List<Appointment> appointments = new ArrayList<>();

        appointments.add(mayAppointment);
        appointments.add(juneAppointment);
        appointments.add(julyAppointment);

        return appointments.stream()
                .filter(a -> Objects.equals(a.getDoctorFirstName(), doctorFirstName) &&
                        Objects.equals(a.getDoctorLastName(), doctorLastName))
                .toList();
    }

    public static List<AppointmentDTO> getAppointmentDTOsByDoctorName() {
        List<Appointment> appointments = getAppointmentsByDoctorName();

        return appointments.stream()
                .filter(a -> Objects.equals(a.getDoctorFirstName(), doctorFirstName) &&
                        Objects.equals(a.getDoctorLastName(), doctorLastName))
                .map(AppointmentDTO::new)
                .toList();
    }
}
