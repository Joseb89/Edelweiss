package com.jaab.edelweiss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.exception.DoctorNotFoundException;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.utils.TestUtils;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class DoctorServiceTest {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager manager;

    private static MockWebServer mockWebServer;

    private static Doctor doctor;

    private static final int USER_API_PORT = 8081;

    @BeforeAll
    public static void init() throws IOException {
        doctor = TestUtils.createDoctor();

        mockWebServer = new MockWebServer();
        mockWebServer.start(USER_API_PORT);
    }

    @AfterAll
    public static void cleanup() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void createDoctorTest() throws JsonProcessingException {
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(doctor)));

        UserDTO userDTO = doctorService.createDoctor(doctor);

        assertEquals(1L, userDTO.getId());
        assertEquals("Wynne", userDTO.getFirstName());
    }

    @Test
    public void updateUserInfoTest() throws JsonProcessingException {
        manager.persist(doctor);

        assertEquals("Langrene", doctor.getLastName());
        assertEquals("spiritoffaith", doctor.getPassword());

        Doctor updatedDoctor = new Doctor(doctor.getId(), null, "Gregoir", null,
                "aneirin", null, null);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(updatedDoctor)));

        Mono<UserDTO> doctorInfo = doctorService.updateUserInfo(updatedDoctor, doctor.getId());

        StepVerifier.create(doctorInfo)
                        .expectNextMatches(userDTO -> Objects.equals(userDTO.getLastName(), "Gregoir"))
                                .verifyComplete();

        assertEquals("Gregoir", doctor.getLastName());
        assertEquals("aneirin", doctor.getPassword());
    }

    @Test
    public void updateUserInfoUserDTONullTest() {
        manager.persist(doctor);

        assertEquals(6687412012L, doctor.getPhoneNumber());

        Doctor updatedDoctor = new Doctor(doctor.getId(), null, null, null,
                null, 5541263307L, null);

        Mono<UserDTO> doctorInfo = doctorService.updateUserInfo(updatedDoctor, doctor.getId());

        StepVerifier.create(doctorInfo)
                .expectNextMatches(userDTO -> Objects.equals(userDTO.getPassword(), null))
                .verifyComplete();
    }

    @Test
    public void deleteUserTest() {
        manager.persist(doctor);

        assertNotNull(doctor);

        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        Mono<Void> deleteDoctor = doctorService.deleteUser(doctor.getId());

        StepVerifier.create(deleteDoctor)
                .verifyComplete();
    }

    @Test
    public void deleteUserExceptionTest() {
        assertThrows(DoctorNotFoundException.class, ()->doctorService.deleteUser(1L).block());
    }
}
