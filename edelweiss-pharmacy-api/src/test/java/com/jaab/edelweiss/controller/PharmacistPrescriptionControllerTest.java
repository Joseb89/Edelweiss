package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.exception.PrescriptionStatusException;
import com.jaab.edelweiss.model.Status;
import com.jaab.edelweiss.service.PharmacistPrescriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;

import java.util.List;

import static com.jaab.edelweiss.utils.TestUtils.pendingPrescriptions;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = PharmacistPrescriptionController.class)
public class PharmacistPrescriptionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PharmacistPrescriptionService pharmacistPrescriptionService;

    @Test
    public void getPendingPrescriptionsTest() {
        List<PrescriptionDTO> getPendingPrescriptions = pendingPrescriptions();

        when(pharmacistPrescriptionService.getPendingPrescriptions())
                .thenReturn(Flux.fromIterable(getPendingPrescriptions));

        webTestClient.get()
                .uri("/pharmacy/home")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PrescriptionDTO.class).hasSize(2);
    }

    @Test
    public void approvePrescriptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(1L, "Rinoa", "Heartily",
                "X-Potion", (byte) 75, Status.DENIED);

        webTestClient.patch()
                .uri("/pharmacy/approvePrescription/" + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void approvePrescriptionExceptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(1L, "Rinoa", "Heartily",
                "X-Potion", (byte) 75, Status.PENDING);

        when(pharmacistPrescriptionService.approvePrescription(any(PrescriptionStatusDTO.class), anyLong()))
                .thenThrow(PrescriptionStatusException.class);

        webTestClient.patch()
                .uri("/pharmacy/approvePrescription/" + 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
