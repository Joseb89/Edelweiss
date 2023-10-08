package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentNotFoundException;
import com.jaab.edelweiss.model.Appointment;
import com.jaab.edelweiss.service.AppointmentService;
import com.jaab.edelweiss.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.time.LocalDate;
import java.time.LocalTime;

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
        AppointmentDTO appointmentDTO = new AppointmentDTO(TestUtils.mayAppointment);

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
                .thenReturn(TestUtils.getAppointmentDTOsByDoctorName());

        webTestClient.get()
                .uri("/physician/myAppointments/" + TestUtils.doctorFirstName + "/" + TestUtils.doctorLastName)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(AppointmentDTO.class).hasSize(2);
    }

    @Test
    public void updateAppointmentInfoTest() {
        Appointment appointment = TestUtils.juneAppointment;

        AppointmentDTO appointmentDTO = new AppointmentDTO(appointment.getId(), appointment.getDoctorFirstName(),
                appointment.getDoctorLastName(), appointment.getPatientFirstName(), appointment.getPatientLastName(),
                LocalDate.of(TestUtils.YEAR, 6, 20), LocalTime.of(11, 30));

        when(appointmentService.updateAppointmentInfo(any(AppointmentDTO.class), anyLong()))
                .thenReturn(appointmentDTO);

        webTestClient.patch()
                .uri("/physician//updateAppointmentInfo/" + appointment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(appointmentDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    public void updateAppointmentInfoExceptionTest() {
        Appointment appointment = TestUtils.juneAppointment;

        AppointmentDTO appointmentDTO = new AppointmentDTO(appointment.getId(), appointment.getDoctorFirstName(),
                appointment.getDoctorLastName(), appointment.getPatientFirstName(), appointment.getPatientLastName(),
                LocalDate.of(TestUtils.YEAR, 6, 20), LocalTime.of(11, 30));

        when(appointmentService.updateAppointmentInfo(any(AppointmentDTO.class), anyLong()))
                .thenThrow(AppointmentNotFoundException.class);

        webTestClient.patch()
                .uri("/physician//updateAppointmentInfo/" + appointment.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(appointmentDTO)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    public void deleteAppointmentTest() {
        webTestClient.delete()
                .uri("/physician/deleteAppointment/" + 1L)
                .exchange()
                .expectStatus().isOk();
    }
}
