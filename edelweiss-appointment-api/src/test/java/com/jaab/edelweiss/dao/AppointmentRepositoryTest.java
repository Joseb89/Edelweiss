package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Appointment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void init() {
        Appointment appointment1 = new Appointment(null, doctorFirstName, doctorLastName,
                "Squall", "Leonheart", LocalDate.of(YEAR, 5, 5),
                LocalTime.of(10, 0));

        Appointment appointment2 = new Appointment(null, doctorFirstName, doctorLastName,
                "Zidaine", "Tribal", LocalDate.of(YEAR, 5, 7),
                LocalTime.of(10, 30));

        Appointment appointment3 = new Appointment(null, "Doctor", "Cid",
                "Vayne", "Solidor", LocalDate.of(YEAR, 3, 6),
                LocalTime.of(9, 0));

        entityManager.persist(appointment1);
        entityManager.persist(appointment2);
        entityManager.persist(appointment3);
    }

    @Test
    public void getAppointmentsByDoctorNameTest() {
        List<Appointment> appointments =
                appointmentRepository
                        .findByDoctorFirstNameAndDoctorLastNameOrderByAppointmentDate(doctorFirstName, doctorLastName);

        assertEquals(2, appointments.size());
    }

    @Test
    public void getAppointmentsByDoctorNameEmptyListTest() {
        List<Appointment> appointments =
                appointmentRepository
                        .findByDoctorFirstNameAndDoctorLastNameOrderByAppointmentDate("Solas", "Wolffe");

        assertEquals(0, appointments.size());
    }
}
