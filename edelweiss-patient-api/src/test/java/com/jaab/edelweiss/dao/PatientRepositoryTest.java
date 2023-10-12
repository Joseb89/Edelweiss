package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void init() {
        Patient james = TestUtils.james;
        Patient bethany = TestUtils.bethany;
        Patient carver = TestUtils.carver;

        james.setId(null);
        bethany.setId(null);
        carver.setId(null);

        Address jamesAddress = TestUtils.jamesAddress;
        Address bethanyAddress = TestUtils.bethanyAddress;
        Address carverAddress = TestUtils.carverAddress;

        entityManager.persist(james);
        entityManager.persist(jamesAddress);
        entityManager.persist(bethany);
        entityManager.persist(bethanyAddress);
        entityManager.persist(carver);
        entityManager.persist(carverAddress);
    }

    @Test
    public void getPatientsByFirstNameTest() {
        List<Patient> patients = patientRepository.getPatientsByFirstName(TestUtils.firstNameTestParameter);
        assertThat(patients.size()).isEqualTo(1);
    }

    @Test
    public void getPatientsByFirstNameEmptyListTest() {
        List<Patient> patients = patientRepository.getPatientsByFirstName("Malcolm");
        assertThat(patients.size()).isEqualTo(0);
    }

    @Test
    public void getPatientsByLastNameTest() {
        List<Patient> patients = patientRepository.getPatientsByLastName(TestUtils.lastNameTestParameter);
        assertThat(patients.size()).isEqualTo(3);
    }

    @Test
    public void getPatientsByLastNameEmptyListTest() {
        List<Patient> patients = patientRepository.getPatientsByLastName("Vallen");
        assertThat(patients.size()).isEqualTo(0);
    }

    @Test
    public void getPatientsByBloodTypeTest() {
        List<Patient> patients = patientRepository.getPatientsByBloodType(TestUtils.bloodTypeTestParameter);
        assertThat(patients.size()).isEqualTo(2);
    }

    @Test
    public void getPatientsByBloodTypeEmptyListTest() {
        List<Patient> patients = patientRepository.getPatientsByBloodType("B+");
        assertThat(patients.size()).isEqualTo(0);
    }
}
