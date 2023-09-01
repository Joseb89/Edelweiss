package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Doctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DoctorRepositoryTest {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    public void init() {
        assertNotNull(doctorRepository);
        assertNotNull(testEntityManager);
    }

    @Test
    public void getDoctorByIdTest() {
        Doctor doctor = new Doctor(1L, "Solas", "Wolffe", "fenharel@gmail.com",
                "iamthedreadwolf", 1174029364L, "Cardiology");

        testEntityManager.persist(doctor);

        Optional<Doctor> getDoctor = doctorRepository.getDoctorById(1L);
        assertThat(getDoctor).isNotEmpty();
        assertThat(getDoctor.get().getPractice()).isEqualTo("Cardiology");
    }

    @Test
    public void getDoctorByIdEmptyTest() {
        Optional<Doctor> getDoctor = doctorRepository.getDoctorById(2L);
        assertThat(getDoctor).isEmpty();
    }
}
