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

import static com.jaab.edelweiss.utils.TestUtils.doctorFirstName;
import static com.jaab.edelweiss.utils.TestUtils.doctorLastName;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PrescriptionRepositoryTest {

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void init() {
        Prescription potion = new Prescription(null, doctorFirstName, doctorLastName,
                "Potion", (byte) 20, Status.PENDING);

        Prescription phoenixDown = new Prescription(null, doctorFirstName, doctorLastName,
                "Phoenix Down", (byte) 50, Status.PENDING);

        Prescription darkMatter = new Prescription(null, "Squall", "Heartily",
                "Dark Matter", (byte) 75, Status.APPROVED);

        entityManager.persist(potion);
        entityManager.persist(phoenixDown);
        entityManager.persist(darkMatter);
    }

    @Test
    public void getPrescriptionsByDoctorNameTest() {
        List<Prescription> prescriptions =
                prescriptionRepository.findByDoctorFirstNameAndDoctorLastName(doctorFirstName, doctorLastName);

        assertEquals(2, prescriptions.size());
    }

    @Test
    public void getPrescriptionsByDoctorNameEmptyListTest() {
        List<Prescription> prescriptions =
                prescriptionRepository.findByDoctorFirstNameAndDoctorLastName("Doctor", "Cid");

        assertEquals(0, prescriptions.size());
    }

    @Test
    public void getPrescriptionsByPrescriptionStatusTest() {
        List<Prescription> prescriptions = prescriptionRepository.findByPrescriptionStatus(Status.APPROVED);

        assertEquals(1, prescriptions.size());
    }

    @Test
    public void getPrescriptionsByPrescriptionStatusEmptyListTest() {
        List<Prescription> prescriptions = prescriptionRepository.findByPrescriptionStatus(Status.DENIED);

        assertEquals(0, prescriptions.size());
    }
}
