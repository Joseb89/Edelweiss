package com.jaab.edelweiss.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.utils.TestUtils;
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

@SpringBootTest
@Transactional
public class DoctorPatientServiceTest {

    @Autowired
    private DoctorPatientService doctorPatientService;

    @Autowired
    private ObjectMapper objectMapper;

    private static MockWebServer mockWebServer;

    private static final int PATIENT_API_PORT = 8082;

    @BeforeAll
    public static void init() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start(PATIENT_API_PORT);
    }

    @AfterAll
    public static void cleanup() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    public void getPatientByIdTest() throws JsonProcessingException {
        PatientDTO patientDTO = new PatientDTO(1L, "Dane", "Cousland",
                "heroofferelden@gmail.com", 8853694771L,
                "Wynne Langrene", "B+");

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(patientDTO)));

        Mono<PatientDTO> getPatient = doctorPatientService.getPatientById(patientDTO.getId());

        StepVerifier.create(getPatient)
                .expectNextMatches(p -> p.getFirstName().equals("Dane") &&
                        p.getLastName().equals("Cousland"))
                .verifyComplete();
    }

    @Test
    public void getPatientsByFirstNameTest() throws JsonProcessingException {
        List<PatientDTO> patients = TestUtils.getPatientsByFirstName();

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(patients)));

        Flux<PatientDTO> getPatients = doctorPatientService.getPatientsByFirstName(TestUtils.firstNameTestParameter);

        StepVerifier.create(getPatients)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void getPatientsByLastNameTest() throws JsonProcessingException {
        List<PatientDTO> patients = TestUtils.getPatientsByLastName();

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(patients)));

        Flux<PatientDTO> getPatients = doctorPatientService.getPatientsByLastName(TestUtils.lastNameTestParameter);

        StepVerifier.create(getPatients)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getPatientsByBloodTypeTest() throws JsonProcessingException {
        List<PatientDTO> patients = TestUtils.getPatientsByBloodType();

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(patients)));

        Flux<PatientDTO> getPatients = doctorPatientService.getPatientsByBloodType(TestUtils.bloodTypeTestParameter);

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

        Mono<AddressDTO> patientAddress = doctorPatientService.getPatientAddress(1L);

        StepVerifier.create(patientAddress)
                .expectNextMatches(a -> a.getCity().equals("Cleveland") &&
                        a.getState().equals("OH"))
                .verifyComplete();
    }

    @Test
    public void deletePatientTest() {
        mockWebServer.enqueue(new MockResponse().setResponseCode(200));

        Mono<Void> deletePatient = doctorPatientService.deletePatient(1L);

        StepVerifier.create(deletePatient)
                .verifyComplete();
    }
}
