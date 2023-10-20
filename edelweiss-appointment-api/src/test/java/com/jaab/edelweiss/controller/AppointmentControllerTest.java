package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentNotFoundException;
import com.jaab.edelweiss.service.AppointmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(controllers = AppointmentController.class)
public class AppointmentControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private AppointmentService appointmentService;

    @Test
    public void createAppointmentTest() {
        AppointmentDTO appointmentDTO = new AppointmentDTO(mayAppointment);

        when(appointmentService.createAppointment(any(AppointmentDTO.class))).thenReturn(appointmentDTO);

        webTestClient.post()
                .uri("/physician/newAppointment")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(appointmentDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    public void getAppointmentsByDoctorNameTest() {
        when(appointmentService.getAppointmentsByDoctorName(anyString(), anyString()))
                .thenReturn(getAppointmentDTOsByDoctorName());

        webTestClient.get()
                .uri("/physician/myAppointments/" + doctorFirstName + "/" + doctorLastName)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AppointmentDTO.class).hasSize(2);
    }

    @Test
    public void updateAppointmentInfoTest() {
        AppointmentDTO appointmentDTO = new AppointmentDTO(juneAppointment.getId(),
                juneAppointment.getDoctorFirstName(), juneAppointment.getDoctorLastName(),
                juneAppointment.getPatientFirstName(), juneAppointment.getPatientLastName(),
                LocalDate.of(YEAR, 6, 20), LocalTime.of(11, 30));

        when(appointmentService.updateAppointmentInfo(any(AppointmentDTO.class), anyLong()))
                .thenReturn(appointmentDTO);

        webTestClient.patch()
                .uri("/physician//updateAppointmentInfo/" + appointmentDTO.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(appointmentDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void updateAppointmentInfoExceptionTest() {
        AppointmentDTO appointmentDTO = new AppointmentDTO(juneAppointment.getId(),
                juneAppointment.getDoctorFirstName(), juneAppointment.getDoctorLastName(),
                juneAppointment.getPatientFirstName(), juneAppointment.getPatientLastName(),
                LocalDate.of(YEAR, 6, 20), LocalTime.of(11, 30));

        when(appointmentService.updateAppointmentInfo(any(AppointmentDTO.class), anyLong()))
                .thenThrow(AppointmentNotFoundException.class);

        webTestClient.patch()
                .uri("/physician//updateAppointmentInfo/" + appointmentDTO.id())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(appointmentDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void deleteAppointmentTest() {
        webTestClient.delete()
                .uri("/physician/deleteAppointment/" + julyAppointment.getId())
                .exchange()
                .expectStatus().isOk();
    }
}
