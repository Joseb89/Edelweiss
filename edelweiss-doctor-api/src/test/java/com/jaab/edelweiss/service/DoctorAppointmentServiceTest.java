package com.jaab.edelweiss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentException;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class DoctorAppointmentServiceTest {

    @Autowired
    private DoctorAppointmentService doctorAppointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager entityManager;

    private Doctor doctor;

    private static MockWebServer mockWebServer;

    private static final int APPOINTMENT_API_PORT = 8085;

    @BeforeAll
    static void setup() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(APPOINTMENT_API_PORT);
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
    public void createAppointmentTest() throws JsonProcessingException {
        AppointmentDTO appointmentDTO = new AppointmentDTO(ID, doctor.getFirstName(), doctor.getLastName(),
                "Dane", "Cousland",
                LocalDate.of(TestUtils.YEAR, 10, 25), LocalTime.of(14, 30));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(appointmentDTO)));

        Mono<AppointmentDTO> newAppointment =
                doctorAppointmentService.createAppointment(appointmentDTO, doctor.getId());

        StepVerifier.create(newAppointment)
                .expectNextMatches(a ->
                        Objects.equals(a.getAppointmentDate(), LocalDate.of(YEAR, 10, 25))
                                &&
                                Objects.equals(a.getAppointmentTime(), LocalTime.of(14, 30)))
                .verifyComplete();
    }

    @Test
    public void createAppointmentDateTimeExceptionTest() {
        AppointmentDTO appointmentDTO = new AppointmentDTO(ID, doctor.getFirstName(), doctor.getLastName(),
                "Dane", "Cousland", LocalDate.of(2023, 6, 10),
                LocalTime.of(13, 30));

        assertThrows(AppointmentException.class, () ->
                doctorAppointmentService.createAppointment(appointmentDTO, doctor.getId()).block());
    }

    @Test
    public void getAppointmentsTest() throws JsonProcessingException {
        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(getAppointments())));

        Flux<AppointmentDTO> getAppointments = doctorAppointmentService.getAppointments(doctor.getId());

        StepVerifier.create(getAppointments)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void updateAppointmentInfoTest() throws JsonProcessingException {
        AppointmentDTO appointmentDTO = new AppointmentDTO(ID, null, null,
                null, null, LocalDate.of(YEAR, 9, 4),
                LocalTime.of(11, 30));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(appointmentDTO)));

        Mono<AppointmentDTO> updatedAppointment =
                doctorAppointmentService.updateAppointmentInfo(appointmentDTO, appointmentDTO.getId());

        StepVerifier.create(updatedAppointment)
                .expectNextMatches(a -> Objects.equals(a.getAppointmentDate(),
                        LocalDate.of(YEAR, 9, 4)))
                .verifyComplete();
    }

    @Test
    public void updateAppointmentInfoDateTimeExceptionTest() {
        AppointmentDTO appointmentDTO = new AppointmentDTO(ID, null, null,
                null, null, LocalDate.of(2023, 5, 4),
                null);

        assertThrows(AppointmentException.class, () ->
                doctorAppointmentService.updateAppointmentInfo(appointmentDTO, doctor.getId()).block());
    }

    @Test
    public void deleteAppointmentTest() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        Mono<Void> deleteAppointment = doctorAppointmentService.deleteAppointment(ID);

        StepVerifier.create(deleteAppointment)
                .verifyComplete();
    }
}
