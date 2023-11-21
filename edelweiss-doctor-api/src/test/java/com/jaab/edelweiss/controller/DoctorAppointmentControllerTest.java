package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentException;
import com.jaab.edelweiss.service.DoctorAppointmentService;
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

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = DoctorAppointmentController.class)
public class DoctorAppointmentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private DoctorAppointmentService doctorAppointmentService;

    @Test
    public void createAppointmentTest() {
        AppointmentDTO appointmentDTO = new AppointmentDTO(ID, "Rinoa", "Heartily",
                "Squall", "Leonheart",
                LocalDate.of(YEAR, 10, 25), LocalTime.of(14, 30));

        webTestClient.post()
                .uri("/physician/newAppointment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(appointmentDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void createAppointmentExceptionTest() {
        AppointmentDTO appointmentDTO = new AppointmentDTO(ID, "Rinoa", "Heartily",
                "Squall", "Leonheart",
                LocalDate.of(2023, 10, 25), LocalTime.of(14, 30));

        when(doctorAppointmentService.createAppointment(any(AppointmentDTO.class)))
                .thenThrow(AppointmentException.class);

        webTestClient.post()
                .uri("/physician/newAppointment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(appointmentDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void getAppointmentsTest() {
        when(doctorAppointmentService.getAppointments())
                .thenReturn(Flux.fromIterable(getAppointments()));

        webTestClient.get()
                .uri("/physician/myAppointments")
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AppointmentDTO.class).hasSize(2);
    }

    @Test
    public void updateAppointmentInfoTest() {
        AppointmentDTO updatedAppointment = new AppointmentDTO(ID, null, null,
                null, null, null, LocalTime.of(15, 0));

        when(doctorAppointmentService.updateAppointmentInfo(any(AppointmentDTO.class), anyLong()))
                .thenReturn(Mono.just(updatedAppointment));

        webTestClient.patch()
                .uri("/physician/updateAppointmentInfo/" + updatedAppointment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedAppointment)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void updateAppointmentInfoExceptionTest() {
        AppointmentDTO updatedAppointment = new AppointmentDTO(ID, null, null,
                null, null, LocalDate.of(2023, 10, 25),
                null);

        when(doctorAppointmentService.updateAppointmentInfo(any(AppointmentDTO.class), anyLong()))
                .thenThrow(AppointmentException.class);

        webTestClient.patch()
                .uri("/physician/updateAppointmentInfo/" + updatedAppointment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(updatedAppointment)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void deleteAppointmentTest() {
        webTestClient.delete()
                .uri("/physician/deleteAppointment/" + ID)
                .exchange()
                .expectStatus().isOk();
    }
}
