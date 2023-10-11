package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;

/**
 * This class serves as a service for retrieving patient information from the patient API
 *
 * @author Joseph Barr
 */
@Service
public class DoctorPatientService {

    private final WebClient webClient;

    public DoctorPatientService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8083/physician").build();
    }

    /**
     * Retrieves a patient from the patient API based on the patient's ID
     *
     * @param patientId - the ID of the patient
     * @return - the patient's information
     */
    public Mono<PatientDTO> getPatientById(Long patientId) {
        return webClient.get()
                .uri("/getPatientById/" + patientId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(PatientDTO.class);
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's first name
     *
     * @param firstName - the first name of the patient
     * @return - the list of patients matching the criteria
     */
    public Flux<PatientDTO> getPatientsByFirstName(String firstName) {
        return getPatientRequest("/getPatientsByFirstName/", firstName);
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's last name
     *
     * @param lastName - the last name of the patient
     * @return - the list of patients matching the criteria
     */
    public Flux<PatientDTO> getPatientsByLastName(String lastName) {
        return getPatientRequest("/getPatientsByLastName/", lastName);
    }

    /**
     * Retrieves a list of patients from the patient API based on the patient's blood type
     *
     * @param bloodType - the blood type of the patient
     * @return - the list of patients matching the criteria
     */
    public Flux<PatientDTO> getPatientsByBloodType(String bloodType) {
        return getPatientRequest("/getPatientsByBloodType/", bloodType);
    }

    /**
     * Retrieves the address of the patient with the specified ID from the patient API
     *
     * @param patientId - the ID of the patient
     * @return - the patient's address
     */
    public Mono<AddressDTO> getPatientAddress(Long patientId) {
        return webClient.get()
                .uri("/getPatientAddress/" + patientId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(AddressDTO.class);
    }

    /**
     * Sends a DELETE request to the patient API to delete the patient with the specified ID
     *
     * @param patientId - the ID of the patient
     * @return - the DELETE request
     */
    public Mono<Void> deletePatient(Long patientId) {
        return webClient.delete()
                .uri("/deletePatient/" + patientId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(Void.class);
    }

    /**
     * Creates a GET request to send to the patient API
     *
     * @param uri       - the specified URI at the patient API
     * @param parameter - the search parameter for retrieving the specified data
     * @return - the GET request
     */
    private Flux<PatientDTO> getPatientRequest(String uri, String parameter) {
        return webClient.get()
                .uri(uri + parameter)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PatientDTO.class);
    }
}
