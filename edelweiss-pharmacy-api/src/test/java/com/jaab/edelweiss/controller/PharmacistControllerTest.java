package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.model.Pharmacist;
import com.jaab.edelweiss.model.Role;
import com.jaab.edelweiss.service.PharmacistService;
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

@WebFluxTest(controllers = PharmacistController.class)
public class PharmacistControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PharmacistService pharmacistService;

    private Pharmacist pharmacist;

    @BeforeEach
    public void init() {
        assertNotNull(webTestClient);
        assertNotNull(pharmacistService);

        pharmacist = TestUtils.createPharmacist();
    }

    @Test
    public void createPharmacistTest() {
        webTestClient.post()
                .uri("/newPharmacist")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(pharmacist)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.role", Matchers.is(Role.PHARMACIST));
    }

    @Test
    public void updatePharmacistInfoTest() {
        Pharmacist updatedPharmacist = new Pharmacist(null, null, null,
                "greywarden@gmail.com", null, null);

        webTestClient.patch()
                .uri("/pharmacy/" + pharmacist.getId() + "/updatePharmacistInfo")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedPharmacist)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.email", Matchers.is("greywarden@gmail.com"));
    }

    @Test
    public void deletePharmacistTest() {
        webTestClient.delete()
                .uri("/pharmacy/deletePharmacist/" + pharmacist.getId())
                .exchange()
                .expectStatus().isOk();
    }
}
