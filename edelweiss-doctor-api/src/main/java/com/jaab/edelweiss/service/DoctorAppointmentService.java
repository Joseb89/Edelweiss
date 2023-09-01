package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.DoctorRepository;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentException;
import com.jaab.edelweiss.model.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;
import java.time.LocalDate;

/**
 * This class serves as a service for creating and maintaining appointment data
 *
 * @author Joseph Barr
 */
@Service
public class DoctorAppointmentService {

    private final WebClient webClient;

    private DoctorRepository doctorRepository;

    @Autowired
    public DoctorAppointmentService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8086/physician").build();
    }

    @Autowired
    public void setDoctorRepository(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    /**
     * Creates a new appointment and sends it to the appointment API
     * @param newAppointment - the AppointmentDTO payload
     * @param physicianId - the ID of the doctor
     * @return - the new appointment
     */
    public Mono<AppointmentDTO> createAppointment(AppointmentDTO newAppointment, Long physicianId) {
        if (newAppointment.getAppointmentDate().isBefore(LocalDate.now()))
            throw new AppointmentException("Appointment date must be today or later date.");

        String[] doctorName = setDoctorName(physicianId);

        newAppointment.setDoctorFirstName(doctorName[0]);
        newAppointment.setDoctorLastName(doctorName[1]);

        return webClient.post()
                .uri("/newAppointment")
                .body(Mono.just(newAppointment), AppointmentDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(AppointmentDTO.class);
    }

    /**
     * Retrieves the appointments from the appointment API for the doctor with the specified ID
     * @param physicianId - the ID of the doctor
     * @return - the list of the appointments
     */
    public Flux<AppointmentDTO> getAppointments(Long physicianId) {
        String[] doctorName = setDoctorName(physicianId);

        return webClient.get()
                .uri("/myAppointments/" + doctorName[0] + "/" + doctorName[1])
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(AppointmentDTO.class);
    }

    /**
     * Updates an appointment with the corresponding ID and sends it to the appointment API
     * @param appointmentDTO - the AppointmentDTO payload containing the updated information
     * @param appointmentId - the ID of the appointment
     * @return - the updated appointment
     */
    public Mono<AppointmentDTO> updateAppointmentInfo(AppointmentDTO appointmentDTO, Long appointmentId) {
        if (appointmentDTO.getAppointmentDate() != null &&
                appointmentDTO.getAppointmentDate().isBefore(LocalDate.now()))
            throw new AppointmentException("Appointment date must be today or later date.");

        appointmentDTO.setId(appointmentId);

        return webClient.patch()
                .uri("/updateAppointmentInfo/" + appointmentId)
                .body(Mono.just(appointmentDTO), AppointmentDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(AppointmentDTO.class);
    }

    /**
     * Sends a DELETE request to the appointment API to delete the appointment with the specified ID
     * @param appointmentId - the ID of the appointment
     * @return - the DELETE request
     */
    public Mono<Void> deleteAppointment(Long appointmentId) {
        return webClient.delete()
                .uri("/deleteAppointment/" + appointmentId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(Void.class);
    }

    /**
     * Retrieves a doctor from the doctor database based on the doctor's ID and sets the doctor's
     * first and last name to a String array
     * @param physicianId - the ID of the doctor
     * @return - the String array containing the doctor's first and last name
     */
    private String[] setDoctorName(Long physicianId) {
        Doctor doctor = doctorRepository.getReferenceById(physicianId);
        String[] doctorName = new String[2];

        doctorName[0] = doctor.getFirstName();
        doctorName[1] = doctor.getLastName();

        return doctorName;
    }
}
