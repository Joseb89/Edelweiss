package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.exception.PatientNotFoundException;
import com.jaab.edelweiss.service.PatientService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = DoctorPatientController.class)
public class DoctorPatientControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PatientService patientService;

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
                .thenReturn(getPatientDTOsByFirstName());

        webTestClient.get()
                .uri("/physician/getPatientsByFirstName/" + firstNameTestParameter)
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
                .thenReturn(getPatientDTOsByLastName());

        webTestClient.get()
                .uri("/physician/getPatientsByLastName/" + lastNameTestParameter)
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
                .thenReturn(getPatientDTOsByBloodType());

        webTestClient.get()
                .uri("/physician/getPatientsByBloodType/" + bloodTypeTestParameter)
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
        AddressDTO addressDTO = new AddressDTO(carverAddress);

        when(patientService.getAddress(anyLong())).thenReturn(addressDTO);

        webTestClient.get()
                .uri("/physician/getPatientAddress/" + carver.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.streetAddress", Matchers.is("59 Gallows St"));
    }

    @Test
    public void getAddressExceptionTest() {
        when(patientService.getAddress(anyLong())).thenThrow(PatientNotFoundException.class);

        webTestClient.get()
                .uri("/physician/getPatientAddress/" + carver.getId())
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void deletePatientTest() {
        webTestClient.delete()
                .uri("/physician/deletePatient/" + carver.getId())
                .exchange()
                .expectStatus().isOk();
    }
}
