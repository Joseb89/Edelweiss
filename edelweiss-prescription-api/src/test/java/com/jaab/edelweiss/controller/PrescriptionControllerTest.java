package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;
import com.jaab.edelweiss.service.PrescriptionService;
import com.jaab.edelweiss.utils.TestUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = PrescriptionController.class)
public class PrescriptionControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private PrescriptionService prescriptionService;

    @Test
    public void createPrescriptionTest() {
        PrescriptionDTO prescriptionDTO = TestUtils.prescriptionDTO;

        when(prescriptionService.createPrescription(any(PrescriptionDTO.class))).thenReturn(prescriptionDTO);

        webTestClient.post()
                .uri("/physician/newPrescription")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.prescriptionName", Matchers.is("Potion"));
    }

    @Test
    public void getPrescriptionsByDoctorNameTest() {
        when(prescriptionService.getPrescriptionsByDoctorName(anyString(), anyString()))
                .thenReturn(TestUtils.getPrescriptionDTOsByDoctorName());

        webTestClient.get()
                .uri("/physician/myPrescriptions/" + TestUtils.doctorFirstName + "/" + TestUtils.doctorLastName)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PrescriptionDTO.class).hasSize(2);
    }

    @Test
    public void getPendingPrescriptionsTest() {
        when(prescriptionService.getPrescriptionsByPrescriptionStatus(any(Status.class)))
                .thenReturn(TestUtils.getPrescriptionDTOsByPendingStatus());

        webTestClient.get()
                .uri("/pharmacy/getPendingPrescriptions")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(PrescriptionDTO.class).hasSize(2);
    }

    @Test
    public void updatePrescriptionInfoTest() {
        Prescription prescription = TestUtils.potion;

        UpdatePrescriptionDTO updatePrescriptionDTO = TestUtils.updatePrescriptionDTO;

        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(prescription.getId(), prescription.getDoctorFirstName(),
                prescription.getDoctorLastName(), updatePrescriptionDTO.prescriptionName(),
                updatePrescriptionDTO.prescriptionDosage(), prescription.getPrescriptionStatus());

        when(prescriptionService.updatePrescriptionInfo(updatePrescriptionDTO, prescription.getId()))
                .thenReturn(prescriptionDTO);

        webTestClient.patch()
                .uri("/physician/updatePrescriptionInfo/" + prescription.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void approvePrescriptionTest() {
        Prescription prescription = TestUtils.phoenixDown;

        PrescriptionStatusDTO status = new PrescriptionStatusDTO(Status.APPROVED);

        PrescriptionDTO prescriptionDTO = new PrescriptionDTO(prescription.getId(), prescription.getDoctorFirstName(),
                prescription.getDoctorLastName(), prescription.getPrescriptionName(),
                prescription.getPrescriptionDosage(), status.prescriptionStatus());

        when(prescriptionService.approvePrescription(status, prescription.getId()))
                .thenReturn(prescriptionDTO);

        webTestClient.patch()
                .uri("/pharmacy/approvePrescription/" + prescription.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(prescriptionDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void deletePrescriptionTest() {
        webTestClient.delete()
                .uri("/physician/deletePrescription/" + 1L)
                .exchange()
                .expectStatus().isOk();
    }
}
