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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
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
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(prescriptionDTO))
                .build());

        PrescriptionDTO newPrescription =
                doctorPrescriptionService.createPrescription(prescriptionDTO);

        when(doctorPrescriptionService.createPrescription(any(PrescriptionDTO.class)))
                .thenReturn(newPrescription);

        assertEquals(newPrescription.getDoctorFirstName(), doctor.getFirstName());
        assertEquals(newPrescription.getDoctorLastName(), doctor.getLastName());
    }

    @Test
    public void createPrescriptionNameExceptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(ID, doctor.getFirstName(), doctor.getLastName(),
                null, (byte) 20, null);

        assertThrows(PrescriptionException.class, () ->
                doctorPrescriptionService.createPrescription(prescriptionDTO));
    }

    @Test
    public void createPrescriptionDosageExceptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(ID, doctor.getFirstName(), doctor.getLastName(),
                "Felandris", null, null);

        assertThrows(PrescriptionException.class, () ->
                doctorPrescriptionService.createPrescription(prescriptionDTO));
    }

    @Test
    public void getPrescriptionsTest() throws JsonProcessingException {
        mockWebServer.enqueue(new MockResponse()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(getPrescriptions()))
                .build());

        List<PrescriptionDTO> getPrescriptions = doctorPrescriptionService.getPrescriptions();

        assertEquals(2, getPrescriptions.size());
    }

    @Test
    public void updatePrescriptionInfoTest() throws JsonProcessingException {
        UpdatePrescriptionDTO updatedPrescription = new UpdatePrescriptionDTO(ID,
                "Dragon's Blood", null);

        mockWebServer.enqueue(new MockResponse()
                .newBuilder()
                .addHeader("Content-Type", "application/json")
                .body(objectMapper.writeValueAsString(updatedPrescription))
                .build());

        UpdatePrescriptionDTO updatePrescription =
                doctorPrescriptionService.updatePrescriptionInfo(updatedPrescription, updatedPrescription.id());

        when(doctorPrescriptionService
                .updatePrescriptionInfo(any(UpdatePrescriptionDTO.class), anyLong()))
                .thenReturn(updatePrescription);
    }

    @Test
    public void updatePrescriptionInfoNameExceptionTest() {
        assertThrows(PrescriptionException.class,
                () -> new UpdatePrescriptionDTO(ID, "", null));
    }

    @Test
    public void updatePrescriptionInfoDosageExceptionTest() {
        assertThrows(PrescriptionException.class,
                () -> new UpdatePrescriptionDTO(ID, null, (byte) -30));
    }

    @Test
    public void deletePrescriptionTest() {
        String deletePrescription = doctorPrescriptionService.deletePrescription(ID);

        MockResponse response = new MockResponse()
                .newBuilder()
                .code(200)
                .body(deletePrescription)
                .build();

        assertEquals(200, response.getCode());
    }
}
