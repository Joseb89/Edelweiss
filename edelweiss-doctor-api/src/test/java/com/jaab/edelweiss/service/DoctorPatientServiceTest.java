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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
        String testParameter = "James";

        List<PatientDTO> patients = createPatients();

        List<PatientDTO> filteredPatients = patients.stream()
                .filter(p -> Objects.equals(p.getFirstName(), testParameter))
                .collect(Collectors.toList());

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(filteredPatients)));

        Flux<PatientDTO> getPatients = doctorPatientService.getPatientsByFirstName(testParameter);

        StepVerifier.create(getPatients)
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    public void getPatientsByLastNameTest() throws JsonProcessingException {
        String testParameter = "Hawke";

        List<PatientDTO> patients = createPatients();

        List<PatientDTO> filteredPatients = patients.stream()
                .filter(p -> Objects.equals(p.getLastName(), testParameter))
                .collect(Collectors.toList());

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(filteredPatients)));

        Flux<PatientDTO> getPatients = doctorPatientService.getPatientsByLastName(testParameter);

        StepVerifier.create(getPatients)
                .expectNextCount(3)
                .verifyComplete();
    }

    @Test
    public void getPatientsByBloodTypeTest() throws JsonProcessingException {
        String testParameter = "AB-";

        List<PatientDTO> patients = createPatients();

        List<PatientDTO> filteredPatients = patients.stream()
                .filter(p -> Objects.equals(p.getBloodType(), testParameter))
                .collect(Collectors.toList());

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setBody(objectMapper.writeValueAsString(filteredPatients)));

        Flux<PatientDTO> getPatients = doctorPatientService.getPatientsByBloodType(testParameter);

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

    private List<PatientDTO> createPatients() {
        PatientDTO james = new PatientDTO(1L, "James", "Hawke",
                "championofkirkwall@gmail.com", 7130042356L,
                "Varric Tethras", "AB+");

        PatientDTO bethany = new PatientDTO(2L, "Bethany", "Hawke",
                "circlemage@yahoo.com", 7130042357L,
                "Varric Tethras", "AB-");

        PatientDTO carver = new PatientDTO(3L, "Carver", "Hawke",
                "templarknight@aol.com", 7130042357L,
                "Varric Tethras", "AB-");

        List<PatientDTO> patients = new ArrayList<>();

        patients.add(james);
        patients.add(bethany);
        patients.add(carver);

        return patients;
    }
}
