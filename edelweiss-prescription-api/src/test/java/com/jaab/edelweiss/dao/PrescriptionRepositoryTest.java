package com.jaab.edelweiss.dao;

import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PrescriptionRepositoryTest {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    public void init() {
        Prescription potion = new Prescription(null, "Rinoa", "Heartily",
                "Potion", (byte) 20, Status.PENDING);

        Prescription phoenixDown = new Prescription(null, "Rinoa", "Heartily",
                "Phoenix Down", (byte) 50, Status.PENDING);

        Prescription darkMatter = new Prescription(null, "Squall", "Heartily",
                "Dark Matter", (byte) 75, Status.APPROVED);

        entityManager.persist(potion);
        entityManager.persist(phoenixDown);
        entityManager.persist(darkMatter);
    }

    @Test
    public void getPrescriptionsByDoctorNameTest() {
        List<Prescription> prescriptions = prescriptionRepository.getPrescriptionsByDoctorName("Rinoa", "Heartily");
        assertEquals(prescriptions.size(), 2);
    }

    @Test
    public void getPrescriptionsByDoctorNameEmptyListTest() {
        List<Prescription> prescriptions = prescriptionRepository.getPrescriptionsByDoctorName("Doctor", "Cid");
        assertEquals(prescriptions.size(), 0);
    }

    @Test
    public void getPrescriptionsByPrescriptionStatusTest() {
        List<Prescription> prescriptions = prescriptionRepository.getPrescriptionsByPrescriptionStatus(Status.APPROVED);
        assertEquals(prescriptions.size(), 1);
    }

    @Test
    public void getPrescriptionsByPrescriptionStatusEmptyListTest() {
        List<Prescription> prescriptions = prescriptionRepository.getPrescriptionsByPrescriptionStatus(Status.DENIED);
        assertEquals(prescriptions.size(), 0);
    }
}
