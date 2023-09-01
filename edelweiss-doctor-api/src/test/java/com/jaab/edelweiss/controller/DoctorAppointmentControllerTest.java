package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.service.DoctorAppointmentService;
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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = DoctorAppointmentController.class)
public class DoctorAppointmentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DoctorAppointmentService doctorAppointmentService;

    @BeforeEach
    public void init() {
        assertNotNull(webTestClient);
        assertNotNull(doctorAppointmentService);
    }

    @Test
    public void createAppointmentTest() {
        AppointmentDTO appointmentDTO = new AppointmentDTO(1L, "Rinoa", "Heartily",
                "Squall", "Leonheart",
                LocalDate.of(TestUtils.YEAR, 10, 25), LocalTime.of(14, 30));

        when(doctorAppointmentService.createAppointment(any(AppointmentDTO.class), anyLong()))
                .thenReturn(Mono.just(appointmentDTO));

        webTestClient.post()
                .uri("/physician/" + TestUtils.ID + "/newAppointment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(appointmentDTO)
                .exchange()
                .expectStatus().isCreated()
                .expectBody().jsonPath("$.lastName", Matchers.is("Heartily"));
    }

    @Test
    public void getAppointmentsTest() {
        List<AppointmentDTO> appointments = TestUtils.getAppointments();

        when(doctorAppointmentService.getAppointments(anyLong())).thenReturn(Flux.fromIterable(appointments));

        webTestClient.get()
                .uri("/physician/" + TestUtils.ID + "/myAppointments")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AppointmentDTO.class).hasSize(2);
    }

    @Test
    public void updateAppointmentInfoTest() {
        AppointmentDTO updatedAppointment = new AppointmentDTO(1L, null, null,
                null, null, null, LocalTime.of(15, 0));

        when(doctorAppointmentService.updateAppointmentInfo(any(AppointmentDTO.class), anyLong()))
                .thenReturn(Mono.just(updatedAppointment));

        webTestClient.patch()
                .uri("/physician/updateAppointmentInfo/" + updatedAppointment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedAppointment)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("$.appointmentTime", Matchers.is("15:00:00"));
    }

    @Test
    public void deleteAppointmentTest() {
        webTestClient.delete()
                .uri("/physician/deleteAppointment/" + TestUtils.ID)
                .exchange()
                .expectStatus().isOk();
    }
}
