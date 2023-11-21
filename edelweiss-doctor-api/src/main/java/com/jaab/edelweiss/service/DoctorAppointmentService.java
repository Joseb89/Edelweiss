package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.dto.LoginDTO;
import com.jaab.edelweiss.exception.AppointmentException;
import com.jaab.edelweiss.utils.AuthUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;
import java.time.LocalDate;

/**
 * This class is a service for creating new appointments and maintaining their information
 *
 * @author Joseph Barr
 */
@Service
public class DoctorAppointmentService {

    private final AuthUtils authUtils;

    private final WebClient webClient;

    public DoctorAppointmentService(AuthUtils authUtils, WebClient.Builder builder) {
        this.authUtils = authUtils;
        this.webClient = builder.baseUrl("http://localhost:8085/physician").build();
    }

    /**
     * Creates a new appointment and sends it to the appointment API
     *
     * @param newAppointment - the AppointmentDTO object
     * @return - the new appointment
     * @throws AppointmentException if the doctor inputs an invalid date for the appointment
     */
    public Mono<AppointmentDTO> createAppointment(AppointmentDTO newAppointment)
            throws AppointmentException {
        if (appointmentDateIsNotValid(newAppointment))
            throw new AppointmentException("Appointment date must be today or later date.");

        LoginDTO loginDTO = authUtils.getUserDetails();

        newAppointment.setDoctorFirstName(loginDTO.firstName());
        newAppointment.setDoctorLastName(loginDTO.lastName());

        return webClient.post()
                .uri("/newAppointment")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newAppointment), AppointmentDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(AppointmentDTO.class);
    }

    /**
     * Retrieves the appointments from the appointment API for the doctor with the specified ID
     *
     * @return - the list of the doctor's appointments
     */
    public Flux<AppointmentDTO> getAppointments() {
        LoginDTO loginDTO = authUtils.getUserDetails();

        return webClient.get()
                .uri("/myAppointments/" + loginDTO.firstName() + "/" + loginDTO.lastName())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(AppointmentDTO.class);
    }

    /**
     * Updates an appointment with the specified ID and sends it to the appointment API
     *
     * @param appointmentDTO - the AppointmentDTO object containing the updated information
     * @param appointmentId  - the ID of the appointment
     * @return - the updated appointment
     * @throws AppointmentException if the doctor inputs an invalid date for the appointment
     */
    public Mono<AppointmentDTO> updateAppointmentInfo(AppointmentDTO appointmentDTO, Long appointmentId)
            throws AppointmentException {
        if (appointmentDTO.getAppointmentDate() != null &&
                appointmentDTO.getAppointmentDate().isBefore(LocalDate.now()))
            throw new AppointmentException("Appointment date must be today or later date.");

        return webClient.patch()
                .uri("/updateAppointmentInfo/" + appointmentId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(appointmentDTO), AppointmentDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(AppointmentDTO.class);
    }

    /**
     * Sends a DELETE request to the appointment API to delete the appointment with the specified ID
     *
     * @param appointmentId - the ID of the appointment
     * @return - the DELETE request
     */
    public Mono<String> deleteAppointment(Long appointmentId) {
        return webClient.delete()
                .uri("/deleteAppointment/" + appointmentId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(String.class);
    }

    /**
     * Checks to see if an appointment date is null or before the current date
     *
     * @param appointmentDTO - the AppointmentDTO object containing the appointment date
     * @return - true if the appointment date is null or before the current date
     */
    private boolean appointmentDateIsNotValid(AppointmentDTO appointmentDTO) {
        return appointmentDTO.getAppointmentDate() == null ||
                appointmentDTO.getAppointmentDate().isBefore(LocalDate.now());
    }
}
