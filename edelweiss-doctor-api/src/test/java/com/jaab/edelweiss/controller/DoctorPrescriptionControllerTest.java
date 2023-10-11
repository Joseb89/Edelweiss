package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.exception.PrescriptionException;
import com.jaab.edelweiss.service.DoctorPrescriptionService;
import com.jaab.edelweiss.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.jaab.edelweiss.utils.TestUtils.ID;
import static com.jaab.edelweiss.utils.TestUtils.getPrescriptions;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = DoctorPrescriptionController.class)
public class DoctorPrescriptionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DoctorPrescriptionService doctorPrescriptionService;

    @Test
    public void createPrescriptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(ID, "Rinoa",
                "Heartily", "X-Potion", (byte) 50, null);

        webTestClient.post()
                .uri("/physician/" + ID + "/newPrescription")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void createPrescriptionExceptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(ID, "Rinoa",
                "Heartily", null, (byte) 50, null);

        when(doctorPrescriptionService.createPrescription(any(PrescriptionDTO.class), anyLong()))
                .thenThrow(PrescriptionException.class);

        webTestClient.post()
                .uri("/physician/" + ID + "/newPrescription")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void getPrescriptionsTest() {
        when(doctorPrescriptionService.getPrescriptions(anyLong()))
                .thenReturn(Flux.fromIterable(getPrescriptions()));

        webTestClient.get()
                .uri("/physician/" + ID + "/myPrescriptions")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PrescriptionDTO.class).hasSize(2);
    }

    @Test
    public void updatePrescriptionInfoTest() {
        UpdatePrescriptionDTO updatedPrescription =
                new UpdatePrescriptionDTO(TestUtils.ID, "Ambrosia", (byte) 40);

        when(doctorPrescriptionService.updatePrescriptionInfo(any(UpdatePrescriptionDTO.class), anyLong()))
                .thenReturn(Mono.just(updatedPrescription));

        webTestClient.patch()
                .uri("/physician/updatePrescriptionInfo/" + updatedPrescription.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedPrescription)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void updatePrescriptionInfoExceptionTest() {
        UpdatePrescriptionDTO updatedPrescription =
                new UpdatePrescriptionDTO(ID, "Ambrosia", (byte) -40);

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
                .uri("/physician/deletePrescription/" + ID)
                .exchange()
                .expectStatus().isOk();
    }
}
