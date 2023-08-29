package com.jaab.edelweiss.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.exception.AppointmentException;
import com.jaab.edelweiss.exception.PrescriptionException;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.model.Status;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import mockwebserver3.MockResponse;
import mockwebserver3.MockWebServer;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class DoctorServiceTest {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EntityManager manager;

    private MockWebServer mockWebServer;

    private Doctor wynne;

    private static final int USER_API_PORT = 8081;

    private static final int PRESCRIPTION_API_PORT = 8085;

    private static final int APPOINTMENT_API_PORT = 8086;

    @BeforeEach
    public void init() {
        wynne = new Doctor(1L, "Wynne", "Langrene", "seniorenchanter@aol.com",
                "spiritoffaith", 6687412012L, "Hematology");
    }

    @Test
    public void createDoctorTest() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(USER_API_PORT);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(wynne)));

        UserDTO userDTO = doctorService.createDoctor(wynne);

        assertEquals(1L, userDTO.getId());
        assertEquals("Wynne", userDTO.getFirstName());

        mockWebServer.shutdown();
    }

    @Test
    public void createPrescriptionTest() throws IOException {
        manager.persist(wynne);

        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(1L, wynne.getFirstName(), wynne.getLastName(),
                "Felandris", (byte) 20, null);

        mockWebServer = new MockWebServer();
        mockWebServer.start(PRESCRIPTION_API_PORT);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(prescriptionDTO)));

        Mono<PrescriptionDTO> newPrescription = doctorService.createPrescription(prescriptionDTO, wynne.getId());

        StepVerifier.create(newPrescription)
                        .expectNextMatches(p -> p.getDoctorFirstName().equals("Wynne") &&
                                            p.getDoctorLastName().equals("Langrene"))
                                .verifyComplete();

        mockWebServer.shutdown();
    }

    @Test
    public void createPrescriptionNameExceptionTest() {
        manager.persist(wynne);

        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(1L, wynne.getFirstName(), wynne.getLastName(),
                "", (byte) 20, null);

        assertThrows(PrescriptionException.class, ()->
                doctorService.createPrescription(prescriptionDTO, wynne.getId()).block());
    }

    @Test
    public void createPrescriptionDosageExceptionTest() {
        manager.persist(wynne);

        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(1L, wynne.getFirstName(), wynne.getLastName(),
                "Felandris", (byte) -20, null);

        assertThrows(PrescriptionException.class, ()->
                doctorService.createPrescription(prescriptionDTO, wynne.getId()).block());
    }

    @Test
    public void createAppointmentTest() throws IOException {
        manager.persist(wynne);

        AppointmentDTO appointmentDTO = new AppointmentDTO(1L, wynne.getFirstName(), wynne.getLastName(),
                "Dane", "Cousland", LocalDate.of(2024, 10, 25),
                LocalTime.of(14, 30));

        mockWebServer = new MockWebServer();
        mockWebServer.start(APPOINTMENT_API_PORT);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(appointmentDTO)));

        Mono<AppointmentDTO> newAppointment = doctorService.createAppointment(appointmentDTO, wynne.getId());

        StepVerifier.create(newAppointment)
                .expectNextMatches(a -> a.getAppointmentDate()
                        .equals(LocalDate.of(2024, 10, 25)) &&
                        a.getAppointmentTime().equals(LocalTime.of(14, 30)))
                        .verifyComplete();

        mockWebServer.shutdown();
    }

    @Test
    public void createAppointmentDateTimeExceptionTest() {
        manager.persist(wynne);

        AppointmentDTO appointmentDTO = new AppointmentDTO(1L, wynne.getFirstName(), wynne.getLastName(),
                "Dane", "Cousland", LocalDate.of(2023, 6, 10),
                LocalTime.of(13, 30));

        assertThrows(AppointmentException.class, ()->
                doctorService.createAppointment(appointmentDTO, wynne.getId()).block());
    }

    @Test
    public void getPrescriptionsTest() throws IOException {
        manager.persist(wynne);

        List<PrescriptionDTO> prescriptions = createPrescriptions();

        List<PrescriptionDTO> wynnePrescriptions = prescriptions.stream()
                .filter(p -> Objects.equals(p.getDoctorFirstName(), "Wynne") &&
                        Objects.equals(p.getDoctorLastName(), "Langrene"))
                .toList();

        mockWebServer = new MockWebServer();
        mockWebServer.start(PRESCRIPTION_API_PORT);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(wynnePrescriptions)));

        Flux<PrescriptionDTO> getPrescriptions = doctorService.getPrescriptions(wynne.getId());

        StepVerifier.create(getPrescriptions)
                        .expectNextCount(2)
                                .verifyComplete();

        mockWebServer.shutdown();
    }

    @Test
    public void getAppointmentsTest() throws IOException {
        manager.persist(wynne);

        List<AppointmentDTO> appointments = createAppointments();

        List<AppointmentDTO> wynneAppointments = appointments.stream()
                .filter(a -> a.getDoctorFirstName().equals("Wynne") &&
                        a.getDoctorLastName().equals("Langrene"))
                .toList();

        mockWebServer = new MockWebServer();
        mockWebServer.start(APPOINTMENT_API_PORT);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(wynneAppointments)));

        Flux<AppointmentDTO> getAppointments = doctorService.getAppointments(wynne.getId());

        StepVerifier.create(getAppointments)
                        .expectNextCount(2)
                                .verifyComplete();

        mockWebServer.shutdown();
    }

    @Test
    public void updateUserInfoTest() throws IOException {
        manager.persist(wynne);

        assertEquals("Langrene", wynne.getLastName());
        assertEquals("spiritoffaith", wynne.getPassword());

        Doctor updatedDoctor = new Doctor(wynne.getId(), null, "Gregoir", null,
                "aneirin", null, null);

        mockWebServer = new MockWebServer();
        mockWebServer.start(USER_API_PORT);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(updatedDoctor)));

        Mono<UserDTO> doctorInfo = doctorService.updateUserInfo(updatedDoctor, wynne.getId());

        StepVerifier.create(doctorInfo)
                        .expectNextMatches(userDTO -> Objects.equals(userDTO.getLastName(), "Gregoir"))
                                .verifyComplete();

        assertEquals("Gregoir", wynne.getLastName());
        assertEquals("aneirin", wynne.getPassword());

        mockWebServer.shutdown();
    }

    @Test
    public void updateUserInfoUserDTONullTest() {
        manager.persist(wynne);

        assertEquals(6687412012L, wynne.getPhoneNumber());

        Doctor updatedDoctor = new Doctor(wynne.getId(), null, null, null,
                null, 5541263307L, null);

        Mono<UserDTO> doctorInfo = doctorService.updateUserInfo(updatedDoctor, wynne.getId());

        StepVerifier.create(doctorInfo)
                .expectNextMatches(userDTO -> Objects.equals(userDTO.getPassword(), null))
                .verifyComplete();
    }

    @Test
    public void updatePrescriptionInfoTest() throws IOException {
        UpdatePrescriptionDTO updatedPrescription = new UpdatePrescriptionDTO(1L,
                "Dragon's Blood", null);

        mockWebServer = new MockWebServer();
        mockWebServer.start(PRESCRIPTION_API_PORT);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(updatedPrescription)));

        Mono<UpdatePrescriptionDTO> updatePrescription = doctorService.updatePrescriptionInfo(updatedPrescription,
                updatedPrescription.getId());

        StepVerifier.create(updatePrescription)
                        .expectNextMatches(u -> Objects.equals(u.getPrescriptionName(), "Dragon's Blood"))
                                .verifyComplete();

        mockWebServer.shutdown();
    }

    @Test
    public void updatePrescriptionInfoNameExceptionTest() {
        UpdatePrescriptionDTO updatedPrescription = new UpdatePrescriptionDTO(1L,
                "", null);

        assertThrows(PrescriptionException.class,
                ()-> doctorService.updatePrescriptionInfo(updatedPrescription, updatedPrescription.getId()).block());
    }

    @Test
    public void updatePrescriptionInfoDosageExceptionTest() {
        UpdatePrescriptionDTO updatedPrescription = new UpdatePrescriptionDTO(1L,
                "Dragon's Blood", (byte) -30);

        assertThrows(PrescriptionException.class,
                ()-> doctorService.updatePrescriptionInfo(updatedPrescription, updatedPrescription.getId()).block());
    }

    private List<PrescriptionDTO> createPrescriptions() {
        PrescriptionDTO felandris = new PrescriptionDTO(1L, "Wynne", "Langrene",
                "Felandris", (byte) 50, Status.PENDING);

        PrescriptionDTO ambrosia = new PrescriptionDTO(2L, "Wynne", "Langrene",
                "Ambrosia", (byte) 60, Status.PENDING);

        PrescriptionDTO lyrium = new PrescriptionDTO(3L, "Solas", "Wolffe",
                "Lyrium", (byte) 75, Status.PENDING);

        List<PrescriptionDTO> prescriptions = new ArrayList<>();

        prescriptions.add(felandris);
        prescriptions.add(ambrosia);
        prescriptions.add(lyrium);

        return prescriptions;
    }

    private List<AppointmentDTO> createAppointments() {
        AppointmentDTO firstAppointment = new AppointmentDTO(1L, "Wynne", "Langrene",
                "Dane", "Cousland", LocalDate.of(2024, 10, 5),
                LocalTime.of(10, 30));

        AppointmentDTO secondAppointment = new AppointmentDTO(2L, "Solas", "Wolffe",
                "Evelyn", "Trevelyan", LocalDate.of(2024, 10, 5),
                LocalTime.of(10, 30));

        AppointmentDTO thirdAppointment = new AppointmentDTO(3L, "Wynne", "Langrene",
                "Alistair", "Theirin", LocalDate.of(2024, 10, 8),
                LocalTime.of(13, 45));

        List<AppointmentDTO> appointments = new ArrayList<>();

        appointments.add(firstAppointment);
        appointments.add(secondAppointment);
        appointments.add(thirdAppointment);

        return appointments;
    }
}
