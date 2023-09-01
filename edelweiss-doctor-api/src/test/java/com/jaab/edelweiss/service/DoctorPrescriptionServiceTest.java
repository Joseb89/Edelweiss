package com.jaab.edelweiss.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.exception.PrescriptionException;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.utils.TestUtils;
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
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class DoctorPrescriptionServiceTest {

    @Autowired
    private DoctorPrescriptionService doctorPrescriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager manager;

    private static MockWebServer mockWebServer;

    private static Doctor doctor;

    private static final int PRESCRIPTION_API_PORT = 8085;

    @BeforeAll
    public static void setup() throws IOException {
        doctor = TestUtils.createDoctor();

        mockWebServer = new MockWebServer();
        mockWebServer.start(PRESCRIPTION_API_PORT);
    }

    @AfterAll
    public static void cleanup() throws IOException {
        mockWebServer.shutdown();
    }

    @BeforeEach
    public void init() {
        manager.persist(doctor);
    }

    @Test
    public void createPrescriptionTest() throws IOException {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(1L, doctor.getFirstName(), doctor.getLastName(),
                "Felandris", (byte) 20, null);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(prescriptionDTO)));

        Mono<PrescriptionDTO> newPrescription =
                doctorPrescriptionService.createPrescription(prescriptionDTO, doctor.getId());

        StepVerifier.create(newPrescription)
                .expectNextMatches(p -> p.getDoctorFirstName().equals("Wynne") &&
                        p.getDoctorLastName().equals("Langrene"))
                .verifyComplete();
    }

    @Test
    public void createPrescriptionNameExceptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(1L, doctor.getFirstName(), doctor.getLastName(),
                "", (byte) 20, null);

        assertThrows(PrescriptionException.class, ()->
                doctorPrescriptionService.createPrescription(prescriptionDTO, doctor.getId()).block());
    }

    @Test
    public void createPrescriptionDosageExceptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(1L, doctor.getFirstName(), doctor.getLastName(),
                "Felandris", (byte) -20, null);

        assertThrows(PrescriptionException.class, ()->
                doctorPrescriptionService.createPrescription(prescriptionDTO, doctor.getId()).block());
    }

    @Test
    public void getPrescriptionsTest() throws IOException {
        List<PrescriptionDTO> prescriptions = TestUtils.getPrescriptions();

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(prescriptions)));

        Flux<PrescriptionDTO> getPrescriptions = doctorPrescriptionService.getPrescriptions(doctor.getId());

        StepVerifier.create(getPrescriptions)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void updatePrescriptionInfoTest() throws IOException {
        UpdatePrescriptionDTO updatedPrescription = new UpdatePrescriptionDTO(1L,
                "Dragon's Blood", null);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(updatedPrescription)));

        Mono<UpdatePrescriptionDTO> updatePrescription =
                doctorPrescriptionService.updatePrescriptionInfo(updatedPrescription, updatedPrescription.getId());

        StepVerifier.create(updatePrescription)
                .expectNextMatches(u -> Objects.equals(u.getPrescriptionName(), "Dragon's Blood"))
                .verifyComplete();
    }

    @Test
    public void updatePrescriptionInfoNameExceptionTest() {
        UpdatePrescriptionDTO updatedPrescription = new UpdatePrescriptionDTO(1L,
                "", null);

        assertThrows(PrescriptionException.class,
                ()-> doctorPrescriptionService
                        .updatePrescriptionInfo(updatedPrescription, updatedPrescription.getId()).block());
    }

    @Test
    public void updatePrescriptionInfoDosageExceptionTest() {
        UpdatePrescriptionDTO updatedPrescription = new UpdatePrescriptionDTO(1L,
                "Dragon's Blood", (byte) -30);

        assertThrows(PrescriptionException.class,
                ()-> doctorPrescriptionService
                        .updatePrescriptionInfo(updatedPrescription, updatedPrescription.getId()).block());
    }

    @Test
    public void deletePrescriptionTest() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        Mono<Void> deletePrescription = doctorPrescriptionService.deletePrescription(1L);

        StepVerifier.create(deletePrescription)
                .verifyComplete();
    }
}
