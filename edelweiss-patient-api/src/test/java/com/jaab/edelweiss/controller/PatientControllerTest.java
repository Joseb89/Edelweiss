package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.exception.PatientNotFoundException;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.service.PatientService;
import com.jaab.edelweiss.utils.TestUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = PatientController.class)
public class PatientControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PatientService patientService;

    private Patient james, bethany, carver;

    @BeforeEach
    public void init() {
        assertNotNull(webTestClient);
        assertNotNull(patientService);

        james = TestUtils.james;

        bethany = TestUtils.bethany;

        carver = TestUtils.carver;
    }

    @Test
    public void createPatientTest() {
        PatientDTO patientDTO = new PatientDTO(james);

        when(patientService.createPatient(any(Patient.class))).thenReturn(patientDTO);

        webTestClient.post()
                .uri("/newPatient")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(patientDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.firstName", Matchers.is("James"));
    }

    @Test
    public void getPatientByIdTest() {
        PatientDTO patientDTO = new PatientDTO(james);

        when(patientService.getPatientById(anyLong())).thenReturn(patientDTO);

        webTestClient.get()
                .uri("/physician/getPatientById/" + patientDTO.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.email", Matchers.is("championofkirkwall@gmail.com"));
    }

    @Test
    public void getPatientByIdExceptionTest() {
        when(patientService.getPatientById(anyLong())).thenThrow(PatientNotFoundException.class);

        webTestClient.get()
                .uri("/physician/getPatientById/" + 1L)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void getPatientsByFirstNameTest() {
        when(patientService.getPatientsByFirstName(anyString()))
                .thenReturn(TestUtils.getPatientDTOsByFirstName());

        webTestClient.get()
                .uri("/physician/getPatientsByFirstName/" + TestUtils.firstNameTestParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PatientDTO.class).hasSize(1);
    }

    @Test
    public void getPatientsByFirstNameExceptionTest() {
        when(patientService.getPatientsByFirstName(anyString())).thenThrow(PatientNotFoundException.class);

        webTestClient.get()
                .uri("/physician/getPatientsByFirstName/Fenris")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void getPatientsByLastNameTest() {
        when(patientService.getPatientsByLastName(anyString()))
                .thenReturn(TestUtils.getPatientDTOsByLastName());

        webTestClient.get()
                .uri("/physician/getPatientsByLastName/" + TestUtils.lastNameTestParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PatientDTO.class).hasSize(3);
    }

    @Test
    public void getPatientsByLastNameExceptionTest() {
        when(patientService.getPatientsByLastName(anyString())).thenThrow(PatientNotFoundException.class);

        webTestClient.get()
                .uri("/physician/getPatientsByLastName/Vallen")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void getPatientsByBloodTypeTest() {
        when(patientService.getPatientsByBloodType(anyString()))
                .thenReturn(TestUtils.getPatientDTOsByBloodType());

        webTestClient.get()
                .uri("/physician/getPatientsByBloodType/" + TestUtils.bloodTypeTestParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PatientDTO.class).hasSize(2);
    }

    @Test
    public void getPatientsByBloodTypeExceptionTest() {
        when(patientService.getPatientsByBloodType(anyString())).thenThrow(PatientNotFoundException.class);

        webTestClient.get()
                .uri("/physician/getPatientsByBloodType/B+")
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void getAddressTest() {
        AddressDTO addressDTO = new AddressDTO(TestUtils.carverAddress);
        when(patientService.getAddress(anyLong())).thenReturn(addressDTO);

        webTestClient.get()
                .uri("/physician/getPatientAddress/" + carver.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.streetAddress", Matchers.is("59 Gallows St"));
    }

    @Test
    public void getAddressEmptyList() {
        when(patientService.getAddress(anyLong())).thenThrow(PatientNotFoundException.class);

        webTestClient.get()
                .uri("/physician/getPatientAddress/" + 4L)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void updateAddressTest() {
        Address updatedAddress = new Address(carver.getId(), null, "654 Adamant Ave", "Boise",
                "ID", 96521);

        webTestClient.patch()
                .uri("/patient/" + carver.getId() +"/updateAddress")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedAddress)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.city", Matchers.is("Boise"));
    }

    @Test
    public void updatePatientInfoTest() {
        Patient updatedInfo = new Patient(bethany.getId(), null, "Amell", null,
                "circlemage", null, null, null, null, null);

        webTestClient.patch()
                .uri("/patient/" + bethany.getId() + "/updatePatientInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedInfo)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.lastName", Matchers.is("Amell"));
    }

    @Test
    public void deletePatientTest() {
        webTestClient.delete()
                .uri("/physician/deletePatient/" + carver.getId())
                .exchange()
                .expectStatus().isOk();
    }
}
