package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;

/**
 * This is a service class that allows pharmacists to communicate with the prescription API
 * to send and receive data
 *
 * @author Joseph Barr
 */
@Service
public class PharmacistPrescriptionService {

    private final WebClient webClient;

    public PharmacistPrescriptionService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8084/pharmacy").build();
    }

    /**
     * Retrieves all the prescriptions from the prescription API with PENDING status
     *
     * @return - the list of pending prescriptions
     */
    public Flux<PrescriptionDTO> getPendingPrescriptions() {
        return webClient.get()
                .uri("/getPendingPrescriptions")
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PrescriptionDTO.class);
    }

    /**
     * Sets an APPROVED or DENIED status for a PrescriptionStatusDTO object and sends it to the prescription API
     *
     * @param status         - the new status of the prescription
     * @param prescriptionId - the ID of the prescription
     * @return - the prescription status
     */
    public Mono<PrescriptionDTO> approvePrescription(PrescriptionStatusDTO status, Long prescriptionId) {
        return webClient.patch()
                .uri("/approvePrescription/" + prescriptionId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(status), PrescriptionStatusDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(PrescriptionDTO.class);
    }
}
