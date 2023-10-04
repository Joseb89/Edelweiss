package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.exception.PrescriptionException;
import com.jaab.edelweiss.service.DoctorPrescriptionService;
import com.jaab.edelweiss.utils.TestUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = DoctorPrescriptionController.class)
public class DoctorPrescriptionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DoctorPrescriptionService doctorPrescriptionService;

    @BeforeEach
    public void init() {
        assertNotNull(webTestClient);
        assertNotNull(doctorPrescriptionService);
    }

    @Test
    public void createPrescriptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(1L, "Rinoa", "Heartily",
                "X-Potion", (byte) 50, null);

        when(doctorPrescriptionService.createPrescription(any(PrescriptionDTO.class), anyLong()))
                .thenReturn(Mono.just(prescriptionDTO));

        webTestClient.post()
                .uri("/physician/" + TestUtils.ID + "/newPrescription")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.doctorFirstName", Matchers.is("Rinoa"));
    }

    @Test
    public void createPrescriptionExceptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(1L, "Rinoa", "Heartily",
                null, (byte) 50, null);

        when(doctorPrescriptionService.createPrescription(any(PrescriptionDTO.class), anyLong()))
                .thenThrow(PrescriptionException.class);

        webTestClient.post()
                .uri("/physician/" + TestUtils.ID + "/newPrescription")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void getPrescriptionsTest() {
        List<PrescriptionDTO> prescriptions = TestUtils.getPrescriptions();

        when(doctorPrescriptionService.getPrescriptions(anyLong()))
                .thenReturn(Flux.fromIterable(prescriptions));

        webTestClient.get()
                .uri("/physician/" + TestUtils.ID + "/myPrescriptions")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PrescriptionDTO.class).hasSize(2);
    }

    @Test
    public void updatePrescriptionInfoTest() {
        UpdatePrescriptionDTO updatedPrescription =
                new UpdatePrescriptionDTO(1L, "Ambrosia", (byte) 40);

        when(doctorPrescriptionService.updatePrescriptionInfo(any(UpdatePrescriptionDTO.class), anyLong()))
                .thenReturn(Mono.just(updatedPrescription));

        webTestClient.patch()
                .uri("/physician/updatePrescriptionInfo/" + updatedPrescription.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedPrescription)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.prescriptionName", Matchers.is("Ambrosia"));
    }

    @Test
    public void updatePrescriptionInfoExceptionTest() {
        UpdatePrescriptionDTO updatedPrescription =
                new UpdatePrescriptionDTO(1L, "Ambrosia", (byte) -40);

        when(doctorPrescriptionService.updatePrescriptionInfo(any(UpdatePrescriptionDTO.class), anyLong()))
                .thenThrow(PrescriptionException.class);

        webTestClient.patch()
                .uri("/physician/updatePrescriptionInfo/" + updatedPrescription.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedPrescription)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void deletePrescriptionTest() {
        webTestClient.delete()
                .uri("/physician/deletePrescription/" + TestUtils.ID)
                .exchange()
                .expectStatus().isOk();
    }
}
