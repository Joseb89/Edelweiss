package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.model.Status;
import com.jaab.edelweiss.service.PrescriptionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = PrescriptionController.class)
public class PrescriptionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PrescriptionService prescriptionService;

    @Test
    public void createPrescriptionTest() {
        when(prescriptionService.createPrescription(any(PrescriptionDTO.class))).thenReturn(prescriptionDTO);

        webTestClient.post()
                .uri("/physician/newPrescription")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void getPrescriptionsByDoctorNameTest() {
        when(prescriptionService.getPrescriptionsByDoctorName(anyString(), anyString()))
                .thenReturn(getPrescriptionDTOsByDoctorName());

        webTestClient.get()
                .uri("/physician/myPrescriptions/" + doctorFirstName + "/" + doctorLastName)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PrescriptionDTO.class).hasSize(2);
    }

    @Test
    public void getPendingPrescriptionsTest() {
        when(prescriptionService.getPrescriptionsByPrescriptionStatus(any(Status.class)))
                .thenReturn(getPrescriptionDTOsByPendingStatus());

        webTestClient.get()
                .uri("/pharmacy/getPendingPrescriptions")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PrescriptionDTO.class).hasSize(2);
    }

    @Test
    public void updatePrescriptionInfoTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(potion.getId(), potion.getDoctorFirstName(),
                potion.getDoctorLastName(), updatePrescriptionDTO.prescriptionName(),
                updatePrescriptionDTO.prescriptionDosage(), potion.getPrescriptionStatus());

        when(prescriptionService.updatePrescriptionInfo(any(UpdatePrescriptionDTO.class), anyLong()))
                .thenReturn(prescriptionDTO);

        webTestClient.patch()
                .uri("/physician/updatePrescriptionInfo/" + prescriptionDTO.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void approvePrescriptionTest() {
        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(phoenixDown.getId(), phoenixDown.getDoctorFirstName(),
                phoenixDown.getDoctorLastName(), phoenixDown.getPrescriptionName(),
                phoenixDown.getPrescriptionDosage(), Status.DENIED);

        when(prescriptionService.approvePrescription(any(PrescriptionStatusDTO.class), anyLong()))
                .thenReturn(prescriptionDTO);

        webTestClient.patch()
                .uri("/pharmacy/approvePrescription/" + prescriptionDTO.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void deletePrescriptionTest() {
        webTestClient.delete()
                .uri("/physician/deletePrescription/" + darkMatter.getId())
                .exchange()
                .expectStatus().isOk();
    }
}
