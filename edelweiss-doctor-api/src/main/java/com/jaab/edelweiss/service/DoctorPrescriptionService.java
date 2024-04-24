package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dto.LoginDTO;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.exception.PrescriptionException;
import com.jaab.edelweiss.utils.AuthUtils;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;
import java.util.List;

/**
 * This class is a service for creating new prescriptions and maintaining their information
 *
 * @author Joseph Barr
 */
@Service
public class DoctorPrescriptionService {

    private final WebClient webClient;

    public DoctorPrescriptionService(WebClient.Builder builder) {
        this.webClient = builder
                .baseUrl("http://localhost:8084/physician")
                .build();
    }

    /**
     * Creates a new prescription and sends it to the prescription API
     *
     * @param newPrescription - the PrescriptionDTO object
     * @return - the new prescription
     * @throws PrescriptionException if the doctor inputs invalid data for the prescription
     */
    public PrescriptionDTO createPrescription(PrescriptionDTO newPrescription)
            throws PrescriptionException {
        if (prescriptionNameIsNotValid(newPrescription))
            throw new PrescriptionException("Please specify prescription name.");

        if (prescriptionDosageIsNotValid(newPrescription))
            throw new PrescriptionException("Prescription dosage must be between 1cc and 127cc.");

        LoginDTO loginDTO = AuthUtils.getUserDetails();

        newPrescription.setDoctorFirstName(loginDTO.firstName());
        newPrescription.setDoctorLastName(loginDTO.lastName());

        return webClient.post()
                .uri("/newPrescription")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(newPrescription), PrescriptionDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(Exception.class).flatMap(Mono::error))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(PrescriptionDTO.class)
                .block();
    }

    /**
     * Retrieves the prescriptions from the prescription API for the doctor with the specified ID
     *
     * @return - the list of the doctor's prescriptions
     */
    public List<PrescriptionDTO> getPrescriptions() {
        LoginDTO loginDTO = AuthUtils.getUserDetails();

        return webClient.get()
                .uri("/myPrescriptions/" + loginDTO.firstName() + "/" + loginDTO.lastName())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PrescriptionDTO.class)
                .collectList()
                .block();
    }

    /**
     * Updates a prescription with the specified ID and sends it to the prescription API
     *
     * @param prescriptionDTO - the UpdatePrescriptionDTO object containing the updated information
     * @param prescriptionId  - the ID of the prescription
     * @return - the updated prescription
     */
    public UpdatePrescriptionDTO updatePrescriptionInfo(UpdatePrescriptionDTO prescriptionDTO,
                                                              Long prescriptionId) {
        return webClient.patch()
                .uri("/updatePrescriptionInfo/" + prescriptionId)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(prescriptionDTO), UpdatePrescriptionDTO.class)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(UpdatePrescriptionDTO.class)
                .block();
    }

    /**
     * Sends a DELETE request to the prescription API to delete the prescription with the specified ID
     *
     * @param prescriptionId - the ID of the prescription
     * @return - the DELETE request
     */
    public String deletePrescription(Long prescriptionId) {
        return webClient.delete()
                .uri("/deletePrescription/" + prescriptionId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Checks to see if a prescription name is null or empty
     *
     * @param prescriptionDTO - the PrescriptionDTO object containing the prescription name
     * @return - true if the prescription name is null or empty
     */
    private boolean prescriptionNameIsNotValid(PrescriptionDTO prescriptionDTO) {
        return prescriptionDTO.getPrescriptionName() == null ||
                prescriptionDTO.getPrescriptionName().isEmpty();
    }

    /**
     * Checks to see if a prescription dosage is null or less than 1cc
     *
     * @param prescriptionDTO - the PrescriptionDTO object containing the prescription dosage
     * @return - true if the prescription dosage is null or less than 1cc
     */
    private boolean prescriptionDosageIsNotValid(PrescriptionDTO prescriptionDTO) {
        return prescriptionDTO.getPrescriptionDosage() == null ||
                prescriptionDTO.getPrescriptionDosage() < 1;
    }
}
