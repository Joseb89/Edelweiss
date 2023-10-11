package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.service.DoctorPatientService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = DoctorPatientController.class)
public class DoctorPatientControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DoctorPatientService doctorPatientService;

    @Test
    public void getPatientByIdTest() {
        PatientDTO patientDTO = new PatientDTO(ID, "Squall", "Leonheart",
                "finalfantasy8@gmail.com", 3304589971L, "Rinoa Heartily", "A+");

        when(doctorPatientService.getPatientById(anyLong())).thenReturn(Mono.just(patientDTO));

        webTestClient.get()
                .uri("/physician/getPatientById/" + patientDTO.id())
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.lastName", Matchers.is("Leonheart"));
    }

    @Test
    public void getPatientsByFirstNameTest() {
        when(doctorPatientService.getPatientsByFirstName(anyString()))
                .thenReturn(Flux.fromIterable(getPatientsByFirstName()));

        webTestClient.get()
                .uri("/physician/getPatientsByFirstName/" + firstNameTestParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PatientDTO.class).hasSize(1);
    }

    @Test
    public void getPatientsByLastNameTest() {
        when(doctorPatientService.getPatientsByLastName(anyString()))
                .thenReturn(Flux.fromIterable(getPatientsByLastName()));

        webTestClient.get()
                .uri("/physician/getPatientsByLastName/" + lastNameTestParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PatientDTO.class).hasSize(3);
    }

    @Test
    public void getPatientsByBloodTypeTest() {
        when(doctorPatientService.getPatientsByBloodType(anyString()))
                .thenReturn(Flux.fromIterable(getPatientsByBloodType()));

        webTestClient.get()
                .uri("/physician/getPatientsByBloodType/" + bloodTypeTestParameter)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PatientDTO.class).hasSize(2);
    }

    @Test
    public void getPatientAddressTest() {
        AddressDTO addressDTO = new AddressDTO("59 Gallows St", "San Antonio",
                "TX", 78615);

        when(doctorPatientService.getPatientAddress(anyLong())).thenReturn(Mono.just(addressDTO));

        webTestClient.get()
                .uri("/physician/getPatientAddress/" + ID)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.streetAddress", Matchers.is("59 Gallows St"));
    }

    @Test
    public void deletePatientTest() {
        webTestClient.delete()
                .uri("/physician/deletePatient/" + ID)
                .exchange()
                .expectStatus().isOk();
    }
}
