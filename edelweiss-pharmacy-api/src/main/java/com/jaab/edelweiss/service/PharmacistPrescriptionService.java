package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.exception.PrescriptionStatusException;
import com.jaab.edelweiss.model.Status;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;

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
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PrescriptionDTO.class);
    }

    /**
     * Sets an APPROVED or DENIED status for a PrescriptionStatusDTO object and sends it to the prescriptionAPI
     *
     * @param status         - the new status of the prescription
     * @param prescriptionId - the ID of the prescription
     * @return - the prescription status
     * @throws PrescriptionStatusException if the status is PENDING
     */
    public Mono<PrescriptionStatusDTO> approvePrescription(PrescriptionStatusDTO status, Long prescriptionId)
            throws PrescriptionStatusException {
        if (status.prescriptionStatus() == Status.PENDING)
            throw new PrescriptionStatusException("Prescription must be approved or denied.");

        return webClient.patch()
                .uri("/approvePrescription/" + prescriptionId)
                .body(Mono.just(status), PrescriptionStatusDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(PrescriptionStatusDTO.class);
    }

}