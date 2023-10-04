package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.exception.PrescriptionException;
import com.jaab.edelweiss.utils.DoctorUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;

/**
 * This class serves as a service for creating new prescriptions and maintaining their information
 *
 * @author Joseph Barr
 */
@Service
public class DoctorPrescriptionService {

    private final DoctorUtils doctorUtils;

    private final WebClient webClient;

    public DoctorPrescriptionService(DoctorUtils doctorUtils, WebClient.Builder builder) {
        this.doctorUtils = doctorUtils;
        this.webClient = builder.baseUrl("http://localhost:8084/physician").build();
    }

    /**
     * Creates a new prescription and sends it to the prescription API
     *
     * @param newPrescription - the PrescriptionDTO payload
     * @param physicianId     - the ID of the doctor
     * @return - the new prescription
     */
    public Mono<PrescriptionDTO> createPrescription(PrescriptionDTO newPrescription, Long physicianId) {
        if (doctorUtils.prescriptionNameIsNotValid(newPrescription))
            throw new PrescriptionException("Please specify prescription name.");

        if (doctorUtils.prescriptionDosageIsNotValid(newPrescription))
            throw new PrescriptionException("Prescription dosage must be between 1cc and 127cc.");

        String[] doctorName = doctorUtils.setDoctorName(physicianId);

        newPrescription.setDoctorFirstName(doctorName[0]);
        newPrescription.setDoctorLastName(doctorName[1]);

        return webClient.post()
                .uri("/newPrescription")
                .body(Mono.just(newPrescription), PrescriptionDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(PrescriptionDTO.class);
    }

    /**
     * Retrieves the prescriptions from the prescription API for the doctor with the specified ID
     *
     * @param physicianId - the ID of the doctor
     * @return - the list of the prescriptions
     */
    public Flux<PrescriptionDTO> getPrescriptions(Long physicianId) {
        String[] doctorName = doctorUtils.setDoctorName(physicianId);

        return webClient.get()
                .uri("/myPrescriptions/" + doctorName[0] + "/" + doctorName[1])
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PrescriptionDTO.class);
    }

    /**
     * Updates a prescription with the corresponding ID and sends it to the prescription API
     *
     * @param prescriptionDTO - the UpdatePrescriptionDTO payload containing the updated information
     * @param prescriptionId  - the ID of the prescription
     * @return - the updated prescription
     */
    public Mono<UpdatePrescriptionDTO> updatePrescriptionInfo(UpdatePrescriptionDTO prescriptionDTO,
                                                              Long prescriptionId) throws PrescriptionException {
        if (prescriptionDTO.prescriptionName() != null &&
                doctorUtils.prescriptionNameIsNotValid(prescriptionDTO))
            throw new PrescriptionException("Please specify prescription name.");

        if (prescriptionDTO.prescriptionDosage() != null &&
                doctorUtils.prescriptionDosageIsNotValid(prescriptionDTO))
            throw new PrescriptionException("Prescription dosage must be between 1cc and 127cc.");

        return webClient.patch()
                .uri("/updatePrescriptionInfo/" + prescriptionId)
                .body(Mono.just(prescriptionDTO), UpdatePrescriptionDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(UpdatePrescriptionDTO.class);
    }

    /**
     * Sends a DELETE request to the prescription API to delete the prescription with the specified ID
     *
     * @param prescriptionId - the ID of the prescription
     * @return - the DELETE request
     */
    public Mono<Void> deletePrescription(Long prescriptionId) {
        return webClient.delete()
                .uri("/deletePrescription/" + prescriptionId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(Void.class);
    }
}
