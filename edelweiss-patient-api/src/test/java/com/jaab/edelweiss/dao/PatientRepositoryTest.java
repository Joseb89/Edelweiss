package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PatientRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    PatientRepository patientRepository;

    Patient james, bethany, carver;

    Address jamesAddress, bethanyAddress, carverAddress;

    @BeforeEach
    public void init() {
        Assertions.assertNotNull(entityManager);
        Assertions.assertNotNull(patientRepository);

        james = new Patient(1L, "James", "Hawke", "championofkirkwall@gmail.com",
                "magerebellion", jamesAddress, 7130042356L,
                "Varric Tethras", "O+");

        bethany = new Patient(2L, "Bethany", "Hawke", "circlemage@gmail.com",
                "daughterofamell", bethanyAddress, 7130042357L,
                "Varric Tethras", "O-");

        carver = new Patient(3L, "Carver", "Hawke", "templarknight@gmail.com",
                "sonofamell", carverAddress, 7130042357L,
                "Varric Tethras", "O-");

        jamesAddress = new Address(james.getId(), james, "58 Hightown Court",
                "San Antonio", "TX", 78615);

        bethanyAddress = new Address(bethany.getId(), bethany, "59 Gallows St",
                "San Antonio", "TX", 78615);

        carverAddress = new Address(carver.getId(), carver, "59 Gallows St",
                "San Antonio", "TX", 78615);

        entityManager.persist(james);
        entityManager.persist(jamesAddress);
        entityManager.persist(bethany);
        entityManager.persist(bethanyAddress);
        entityManager.persist(carver);
        entityManager.persist(carverAddress);
    }

    @Test
    public void getPatientByIdTest() {
        Optional<Patient> getPatient = patientRepository.getPatientById(1L);
        assertThat(getPatient).isNotEmpty();
        assertThat(getPatient.get().getFirstName()).isEqualTo("James");
    }

    @Test
    public void getPatientByIdEmptyTest() {
        Optional<Patient> getPatient = patientRepository.getPatientById(4L);
        assertThat(getPatient).isEmpty();
    }

    @Test
    public void getPatientsByFirstNameTest() {
        List<Patient> patients = patientRepository.getPatientsByFirstName("James");
        assertThat(patients.size()).isEqualTo(1);
    }

    @Test
    public void getPatientsByFirstNameEmptyListTest() {
        List<Patient> patients = patientRepository.getPatientsByFirstName("Malcolm");
        assertThat(patients.size()).isEqualTo(0);
    }

    @Test
    public void getPatientsByLastNameTest() {
        List<Patient> patients = patientRepository.getPatientsByLastName("Hawke");
        assertThat(patients.size()).isEqualTo(3);
    }

    @Test
    public void getPatientsByLastNameEmptyListTest() {
        List<Patient> patients = patientRepository.getPatientsByLastName("Vallen");
        assertThat(patients.size()).isEqualTo(0);
    }

    @Test
    public void getPatientsByBloodTypeTest() {
        List<Patient> patients = patientRepository.getPatientsByBloodType("O-");
        assertThat(patients.size()).isEqualTo(2);
    }

    @Test
    public void getPatientsByBloodTypeEmptyListTest() {
        List<Patient> patients = patientRepository.getPatientsByBloodType("B+");
        assertThat(patients.size()).isEqualTo(0);
    }
}
