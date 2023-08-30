package com.jaab.edelweiss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentException;
import com.jaab.edelweiss.model.Doctor;
import jakarta.persistence.EntityManager;
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
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class DoctorAppointmentServiceTest {

    @Autowired
    private DoctorAppointmentService doctorAppointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager manager;

    private static MockWebServer mockWebServer;

    private static Doctor wynne;

    private static final int APPOINTMENT_API_PORT = 8086;

    private static final int year = LocalDate.now().getYear();

    @BeforeAll
    public static void init() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(APPOINTMENT_API_PORT);

        wynne = new Doctor(1L, "Wynne", "Langrene", "seniorenchanter@aol.com",
                "spiritoffaith", 6687412012L, "Hematology");
    }

    @AfterAll
    public static void cleanup() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void createAppointmentTest() throws JsonProcessingException {
        manager.persist(wynne);

        AppointmentDTO appointmentDTO = new AppointmentDTO(1L, wynne.getFirstName(), wynne.getLastName(),
                "Dane", "Cousland", LocalDate.of((year + 1), 10, 25),
                LocalTime.of(14, 30));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(appointmentDTO)));

        Mono<AppointmentDTO> newAppointment =
                doctorAppointmentService.createAppointment(appointmentDTO, wynne.getId());

        StepVerifier.create(newAppointment)
                .expectNextMatches(a -> a.getAppointmentDate()
                        .equals(LocalDate.of((year + 1), 10, 25)) &&
                        a.getAppointmentTime().equals(LocalTime.of(14, 30)))
                .verifyComplete();
    }

    @Test
    public void createAppointmentDateTimeExceptionTest() {
        manager.persist(wynne);

        AppointmentDTO appointmentDTO = new AppointmentDTO(1L, wynne.getFirstName(), wynne.getLastName(),
                "Dane", "Cousland", LocalDate.of((year - 1), 6, 10),
                LocalTime.of(13, 30));

        assertThrows(AppointmentException.class, () ->
                doctorAppointmentService.createAppointment(appointmentDTO, wynne.getId()).block());
    }

    @Test
    public void getAppointmentsTest() throws JsonProcessingException {
        manager.persist(wynne);

        List<AppointmentDTO> appointments = createAppointments();

        List<AppointmentDTO> wynneAppointments = appointments.stream()
                .filter(a -> a.getDoctorFirstName().equals("Wynne") &&
                        a.getDoctorLastName().equals("Langrene"))
                .toList();

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(wynneAppointments)));

        Flux<AppointmentDTO> getAppointments = doctorAppointmentService.getAppointments(wynne.getId());

        StepVerifier.create(getAppointments)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void updateAppointmentInfoTest() throws JsonProcessingException {
        AppointmentDTO appointmentDTO = new AppointmentDTO(1L, "Solas", "Wolffe",
                "Evelyn", "Trevelyan", LocalDate.of((year + 1), 9, 4),
                LocalTime.of(11, 30));

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(appointmentDTO)));

        Mono<AppointmentDTO> updatedAppointment =
                doctorAppointmentService.updateAppointmentInfo(appointmentDTO, appointmentDTO.getId());

        StepVerifier.create(updatedAppointment)
                .expectNextMatches(a -> Objects.equals(appointmentDTO.getAppointmentDate(),
                        LocalDate.of((year + 1), 9, 4)))
                .verifyComplete();
    }

    @Test
    public void deleteAppointmentTest() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        Mono<Void> deleteAppointment = doctorAppointmentService.deleteAppointment(1L);

        StepVerifier.create(deleteAppointment)
                .verifyComplete();
    }

    private List<AppointmentDTO> createAppointments() {
        AppointmentDTO firstAppointment = new AppointmentDTO(1L, "Wynne", "Langrene",
                "Dane", "Cousland", LocalDate.of((year + 1), 10, 5),
                LocalTime.of(10, 30));

        AppointmentDTO secondAppointment = new AppointmentDTO(2L, "Solas", "Wolffe",
                "Evelyn", "Trevelyan", LocalDate.of((year + 1), 10, 5),
                LocalTime.of(10, 30));

        AppointmentDTO thirdAppointment = new AppointmentDTO(3L, "Wynne", "Langrene",
                "Alistair", "Theirin", LocalDate.of((year + 1), 10, 8),
                LocalTime.of(13, 45));

        List<AppointmentDTO> appointments = new ArrayList<>();

        appointments.add(firstAppointment);
        appointments.add(secondAppointment);
        appointments.add(thirdAppointment);

        return appointments;
    }
}
