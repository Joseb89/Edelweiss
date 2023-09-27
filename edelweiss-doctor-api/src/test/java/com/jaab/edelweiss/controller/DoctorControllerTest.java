package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.service.DoctorService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DoctorService doctorService;

    private Doctor doctor;

    @BeforeEach
    public void init() {
        assertNotNull(webTestClient);
        assertNotNull(doctorService);

        doctor = TestUtils.createDoctor(TestUtils.ID);
    }

    @Test
    public void createPhysicianTest() {
        when(doctorService.createDoctor(any(Doctor.class))).thenReturn(doctor);

        webTestClient.post()
                .uri("/newPhysician")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(doctor)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.id", Matchers.is(1L));
    }

    @Test
    public void updateDoctorInfoTest() {
        Doctor updatedInfo = new Doctor(doctor.getId(), null, null, "archmage@aol.com",
                null, null, null, null);

        webTestClient.patch()
                .uri("/physician/" + doctor.getId() + "/updatePhysicianInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedInfo)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.email", Matchers.is("archmage@aol.com"));
    }

    @Test
    public void deleteDoctorTest() {
        webTestClient.delete()
                .uri("/physician/deleteDoctor/" + doctor.getId())
                .exchange()
                .expectStatus().isOk();
    }
}
