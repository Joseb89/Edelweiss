package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void init() {
        james.setId(null);
        bethany.setId(null);
        carver.setId(null);

        entityManager.persist(james);
        entityManager.persist(jamesAddress);
        entityManager.persist(bethany);
        entityManager.persist(bethanyAddress);
        entityManager.persist(carver);
        entityManager.persist(carverAddress);
    }

    @Test
    public void getPatientsByFirstNameTest() {
        List<Patient> patients = patientRepository.findByFirstNameOrderByLastName(firstNameTestParameter);

        assertEquals(1, patients.size());
    }

    @Test
    public void getPatientsByFirstNameEmptyListTest() {
        List<Patient> patients = patientRepository.findByFirstNameOrderByLastName("Malcolm");

        assertEquals(0, patients.size());
    }

    @Test
    public void getPatientsByLastNameTest() {
        List<Patient> patients = patientRepository.findByLastNameOrderByFirstName(lastNameTestParameter);

        assertEquals(3, patients.size());
    }

    @Test
    public void getPatientsByLastNameEmptyListTest() {
        List<Patient> patients = patientRepository.findByLastNameOrderByFirstName("Vallen");

        assertEquals(0, patients.size());
    }

    @Test
    public void getPatientsByBloodTypeTest() {
        List<Patient> patients = patientRepository.findByBloodType(bloodTypeTestParameter);

        assertEquals(2, patients.size());
    }

    @Test
    public void getPatientsByBloodTypeEmptyListTest() {
        List<Patient> patients = patientRepository.findByBloodType("B+");

        assertEquals(0, patients.size());
    }
}
