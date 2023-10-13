package com.jaab.edelweiss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.exception.PrescriptionStatusException;
import com.jaab.edelweiss.model.Status;
import jakarta.transaction.Transactional;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static com.jaab.edelweiss.utils.TestUtils.pendingPrescriptions;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class PharmacistPrescriptionServiceTest {

    @Autowired
    private PharmacistPrescriptionService pharmacistPrescriptionService;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockWebServer mockWebServer;

    private static final int PRESCRIPTION_API_PORT = 8084;

    @BeforeAll
    static void init() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(PRESCRIPTION_API_PORT);
    }

    @AfterAll
    static void cleanup() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getPendingPrescriptionsTest() throws JsonProcessingException {
        List<PrescriptionDTO> pendingPrescriptions = pendingPrescriptions();

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(pendingPrescriptions)));

        Flux<PrescriptionDTO> getPendingPrescriptions = pharmacistPrescriptionService.getPendingPrescriptions();

        StepVerifier.create(getPendingPrescriptions)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void approvePrescriptionTest() throws JsonProcessingException {
        PrescriptionStatusDTO status = new PrescriptionStatusDTO(Status.APPROVED);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(status)));

        Mono<PrescriptionStatusDTO> prescriptionStatus =
                pharmacistPrescriptionService.approvePrescription(status, 1L);

        StepVerifier.create(prescriptionStatus)
                .expectNextMatches(s -> Objects.equals(s.prescriptionStatus(), Status.APPROVED))
                .verifyComplete();
    }

    @Test
    public void approvePrescriptionExceptionTest() {
        PrescriptionStatusDTO status = new PrescriptionStatusDTO(Status.PENDING);

        assertThrows(PrescriptionStatusException.class,
                () -> pharmacistPrescriptionService.approvePrescription(status, 1L).block());
    }
}
