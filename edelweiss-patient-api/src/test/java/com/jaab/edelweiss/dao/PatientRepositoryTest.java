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
        List<Patient> patients = patientRepository.getPatientsByFirstName(firstNameTestParameter);

        assertEquals(1, patients.size());
    }

    @Test
    public void getPatientsByFirstNameEmptyListTest() {
        List<Patient> patients = patientRepository.getPatientsByFirstName("Malcolm");

        assertEquals(0, patients.size());
    }

    @Test
    public void getPatientsByLastNameTest() {
        List<Patient> patients = patientRepository.getPatientsByLastName(lastNameTestParameter);

        assertEquals(3, patients.size());
    }

    @Test
    public void getPatientsByLastNameEmptyListTest() {
        List<Patient> patients = patientRepository.getPatientsByLastName("Vallen");

        assertEquals(0, patients.size());
    }

    @Test
    public void getPatientsByBloodTypeTest() {
        List<Patient> patients = patientRepository.getPatientsByBloodType(bloodTypeTestParameter);

        assertEquals(2, patients.size());
    }

    @Test
    public void getPatientsByBloodTypeEmptyListTest() {
        List<Patient> patients = patientRepository.getPatientsByBloodType("B+");

        assertEquals(0, patients.size());
    }
}
