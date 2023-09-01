package com.jaab.edelweiss.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.exception.PatientNotFoundException;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class PatientServiceTest {

    @Autowired
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager manager;

    private MockWebServer mockWebServer;

    private Patient james, bethany, carver;

    private Address jamesAddress, bethanyAddress, carverAddress;

    private static final int USER_API_PORT = 8081;

    @BeforeEach
    public void init() {
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
    }

    @Test
    public void createPatientTest() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(USER_API_PORT);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(james)));

        james.setAddress(jamesAddress);
        UserDTO userDTO = patientService.createPatient(james);

        assertEquals(1L, userDTO.getId());
        assertEquals("James", userDTO.getFirstName());

        mockWebServer.shutdown();
    }

    @Test
    public void getPatientByIdTest() {
        james.setAddress(jamesAddress);
        manager.persist(james);

        PatientDTO patientDTO = patientService.getPatientById(james.getId());

        assertEquals("James", patientDTO.getFirstName());
        assertEquals("championofkirkwall@gmail.com", patientDTO.getEmail());
    }

    @Test
    public void getPatientByIdExceptionTest() {
        james.setAddress(jamesAddress);
        manager.persist(james);

        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientById(bethany.getId()));
    }

    @Test
    public void getPatientsByFirstNameTest() {
        james.setAddress(jamesAddress);
        bethany.setAddress(bethanyAddress);
        carver.setAddress(carverAddress);

        manager.persist(james);
        manager.persist(bethany);
        manager.persist(carver);

        List<PatientDTO> patients = patientService.getPatientsByFirstName("Bethany");
        assertEquals(1, patients.size());
    }

    @Test
    public void getPatientsByFirstNameExceptionTest() {
        james.setAddress(jamesAddress);
        bethany.setAddress(bethanyAddress);
        carver.setAddress(carverAddress);

        manager.persist(james);
        manager.persist(bethany);
        manager.persist(carver);

        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientsByFirstName("Fenris"));
    }

    @Test
    public void getPatientsByLastNameTest() {
        james.setAddress(jamesAddress);
        bethany.setAddress(bethanyAddress);
        carver.setAddress(carverAddress);

        manager.persist(james);
        manager.persist(bethany);
        manager.persist(carver);

        List<PatientDTO> patients = patientService.getPatientsByLastName("Hawke");
        assertEquals(3, patients.size());
    }

    @Test
    public void getPatientsByLastNameExceptionTest() {
        james.setAddress(jamesAddress);
        bethany.setAddress(bethanyAddress);
        carver.setAddress(carverAddress);

        manager.persist(james);
        manager.persist(bethany);
        manager.persist(carver);

        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientsByLastName("Vallen"));
    }

    @Test
    public void getPatientsByBloodTypeTest() {
        james.setAddress(jamesAddress);
        bethany.setAddress(bethanyAddress);
        carver.setAddress(carverAddress);

        manager.persist(james);
        manager.persist(bethany);
        manager.persist(carver);

        List<PatientDTO> patients = patientService.getPatientsByBloodType("O-");
        assertEquals(2, patients.size());
    }

    @Test
    public void getPatientsByBloodTypeExceptionTest() {
        james.setAddress(jamesAddress);
        bethany.setAddress(bethanyAddress);
        carver.setAddress(carverAddress);

        manager.persist(james);
        manager.persist(bethany);
        manager.persist(carver);

        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientsByBloodType("B+"));
    }

    @Test
    public void getAddressTest() {
        bethany.setAddress(bethanyAddress);
        manager.persist(bethany);

        AddressDTO addressDTO = patientService.getAddress(bethany.getId());

        assertEquals("59 Gallows St", addressDTO.getStreetAddress());
        assertEquals("San Antonio", addressDTO.getCity());
    }

    @Test
    public void updateAddressTest() {
        carver.setAddress(carverAddress);
        manager.persist(carver);

        assertEquals("San Antonio", carver.getAddress().getCity());
        assertEquals("TX", carver.getAddress().getState());

        Address updatedAddress = new Address(carver.getId(), carver, "515 Weisshaupt Ct",
                "Boise", "ID", 33247);

        patientService.updateAddress(updatedAddress, carver.getId());

        assertEquals("Boise", carver.getAddress().getCity());
        assertEquals("ID", carver.getAddress().getState());
    }

    @Test
    public void updatePatientInfoTest() throws IOException {
        bethany.setAddress(bethanyAddress);
        manager.persist(bethany);

        assertEquals("circlemage@gmail.com", bethany.getEmail());
        assertEquals("daughterofamell", bethany.getPassword());

        Patient updatedpatient = new Patient(bethany.getId(), null, null,
                "sisterofthechampion@yahoo.com", "malcomsheir", null,
                null, null, null);

        mockWebServer = new MockWebServer();
        mockWebServer.start(USER_API_PORT);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(updatedpatient)));

        Mono<UserDTO> patientInfo = patientService.updateUserInfo(updatedpatient, bethany.getId());

        StepVerifier.create(patientInfo)
                .expectNextMatches(userDTO -> userDTO.getEmail().equals("sisterofthechampion@yahoo.com"))
                .verifyComplete();

        assertEquals("sisterofthechampion@yahoo.com", bethany.getEmail());
        assertEquals("malcomsheir", bethany.getPassword());

        mockWebServer.shutdown();
    }

    @Test
    public void updateUserInfoUserDTONullTest() {
        bethany.setAddress(bethanyAddress);
        manager.persist(bethany);

        assertEquals(7130042357L, bethany.getPhoneNumber());

        Patient updatedPatient = new Patient(bethany.getId(), null, null, null, null,
                null, 5569034478L, null, null);

        Mono<UserDTO> patientInfo = patientService.updateUserInfo(updatedPatient, bethany.getId());

        StepVerifier.create(patientInfo)
                .expectNextMatches(p -> Objects.equals(p.getPassword(), null))
                .verifyComplete();
    }

    @Test
    public void deleteUserTest() throws IOException {
        james.setAddress(jamesAddress);
        manager.persist(james);

        assertNotNull(james);

        mockWebServer = new MockWebServer();
        mockWebServer.start(USER_API_PORT);

        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        Mono<Void> deletePatient = patientService.deleteUser(james.getId());

        StepVerifier.create(deletePatient)
                .verifyComplete();

        mockWebServer.shutdown();
    }
}
