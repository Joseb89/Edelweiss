package com.jaab.edelweiss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
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

import static com.jaab.edelweiss.utils.TestUtils.*;

@SpringBootTest
@Transactional
public class DoctorPatientServiceTest {

    @Autowired
    private DoctorPatientService doctorPatientService;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockWebServer mockWebServer;

    private static final int PATIENT_API_PORT = 8083;

    @BeforeAll
    static void init() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(PATIENT_API_PORT);
    }

    @AfterAll
    static void cleanup() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getPatientByIdTest() throws JsonProcessingException {
        PatientDTO patientDTO = new PatientDTO(ID, "Dane", "Cousland",
                "heroofferelden@gmail.com", 8853694771L,
                "Wynne Langrene", "B+");

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(patientDTO)));

        Mono<PatientDTO> getPatient = doctorPatientService.getPatientById(patientDTO.id());

        StepVerifier.create(getPatient)
                .expectNextMatches(p -> Objects.equals(p.firstName(), "Dane") &&
                        Objects.equals(p.lastName(), "Cousland"))
                .verifyComplete();
    }

    @Test
    public void getPatientsByFirstNameTest() throws JsonProcessingException {
        List<PatientDTO> patients = getPatientsByFirstName();

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(patients)));

        Flux<PatientDTO> getPatients =
                doctorPatientService.getPatientsByFirstName(firstNameTestParameter);

        StepVerifier.create(getPatients)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void getPatientsByLastNameTest() throws JsonProcessingException {
        List<PatientDTO> patients = getPatientsByLastName();

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(patients)));

        Flux<PatientDTO> getPatients =
                doctorPatientService.getPatientsByLastName(lastNameTestParameter);

        StepVerifier.create(getPatients)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getPatientsByBloodTypeTest() throws JsonProcessingException {
        List<PatientDTO> patients = getPatientsByBloodType();

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(patients)));

        Flux<PatientDTO> getPatients =
                doctorPatientService.getPatientsByBloodType(bloodTypeTestParameter);

        StepVerifier.create(getPatients)
                .expectNextCount(2)
                .verifyComplete();
    }

    @Test
    public void getPatientAddressTest() throws JsonProcessingException {
        AddressDTO addressDTO = new AddressDTO("210 Denerim Drive", "Cleveland",
                "OH", 22354);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(addressDTO)));

        Mono<AddressDTO> patientAddress = doctorPatientService.getPatientAddress(ID);

        StepVerifier.create(patientAddress)
                .expectNextMatches(a -> Objects.equals(a.city(), "Cleveland") &&
                        Objects.equals(a.state(), "OH"))
                .verifyComplete();
    }

    @Test
    public void deletePatientTest() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        Mono<Void> deletePatient = doctorPatientService.deletePatient(ID);

        StepVerifier.create(deletePatient)
                .verifyComplete();
    }
}
