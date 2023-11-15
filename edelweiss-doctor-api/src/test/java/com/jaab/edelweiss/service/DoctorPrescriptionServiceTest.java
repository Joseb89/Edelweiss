package com.jaab.edelweiss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.exception.PrescriptionException;
import com.jaab.edelweiss.model.Doctor;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.Objects;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class DoctorPrescriptionServiceTest {

    @Autowired
    private DoctorPrescriptionService doctorPrescriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    private Doctor doctor;

    private static MockWebServer mockWebServer;

    private static final int PRESCRIPTION_API_PORT = 8084;

    @BeforeAll
    static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(PRESCRIPTION_API_PORT);
    }

    @AfterAll
    static void cleanup() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    void init() {
        doctor = createDoctor(null);

        entityManager.persist(doctor);
    }

    @Test
    public void createPrescriptionTest() throws JsonProcessingException {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(ID, doctor.getFirstName(), doctor.getLastName(),
                "Felandris", (byte) 20, null);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(prescriptionDTO)));

        Mono<PrescriptionDTO> newPrescription =
                doctorPrescriptionService.createPrescription(prescriptionDTO, doctor.getId());

        StepVerifier.create(newPrescription)
                .expectNextMatches(p -> Objects.equals(p.getDoctorFirstName(), doctorFirstName) &&
                        Objects.equals(p.getDoctorLastName(), doctorLastName))
                .verifyComplete();
    }

    @Test
    public void createPrescriptionNameExceptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(ID, doctor.getFirstName(), doctor.getLastName(),
                null, (byte) 20, null);

        assertThrows(PrescriptionException.class, () ->
                doctorPrescriptionService.createPrescription(prescriptionDTO, doctor.getId()).block());
    }

    @Test
    public void createPrescriptionDosageExceptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(ID, doctor.getFirstName(), doctor.getLastName(),
                "Felandris", null, null);

        assertThrows(PrescriptionException.class, () ->
                doctorPrescriptionService.createPrescription(prescriptionDTO, doctor.getId()).block());
    }

    @Test
    public void getPrescriptionsTest() throws JsonProcessingException {
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(getPrescriptions())));

        Flux<PrescriptionDTO> getPrescriptions = doctorPrescriptionService.getPrescriptions(doctor.getId());

        StepVerifier.create(getPrescriptions)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void updatePrescriptionInfoTest() throws JsonProcessingException {
        UpdatePrescriptionDTO updatedPrescription = new UpdatePrescriptionDTO(ID,
                "Dragon's Blood", null);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(updatedPrescription)));

        Mono<UpdatePrescriptionDTO> updatePrescription =
                doctorPrescriptionService.updatePrescriptionInfo(updatedPrescription, updatedPrescription.id());

        StepVerifier.create(updatePrescription)
                .expectNextMatches(u -> Objects.equals(u.prescriptionName(), "Dragon's Blood"))
                .verifyComplete();
    }

    @Test
    public void updatePrescriptionInfoNameExceptionTest() {
        assertThrows(PrescriptionException.class,
                () -> new UpdatePrescriptionDTO(ID,"", null));
    }

    @Test
    public void updatePrescriptionInfoDosageExceptionTest() {
        assertThrows(PrescriptionException.class,
                () -> new UpdatePrescriptionDTO(ID,null, (byte) -30));
    }

    @Test
    public void deletePrescriptionTest() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        Mono<String> deletePrescription = doctorPrescriptionService.deletePrescription(ID);

        StepVerifier.create(deletePrescription)
                .verifyComplete();
    }
}
