package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PharmacistRepository;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Pharmacist;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;

@Service
public class PharmacistService {

    private final WebClient webClient;

    private PharmacistRepository pharmacistRepository;

    private static final String USER_API_URL = "http://localhost:8081";

    private static final String PRESCRIPTION_API_URL = "http://localhost:8085/pharmacy";

    @Autowired
    public PharmacistService(WebClient.Builder builder) {
        this.webClient = builder.build();
    }

    @Autowired
    public void setPharmacistRepository(PharmacistRepository pharmacistRepository) {
        this.pharmacistRepository = pharmacistRepository;
    }

    /**
     * Saves a new pharmacist to the pharmacist database
     * @param pharmacist - the new Pharmacist
     * @return - the UserDTO payload
     */
    public UserDTO createPharmacist(Pharmacist pharmacist) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(pharmacist, userDTO);
        UserDTO userData = sendUserData(userDTO);
        pharmacist.setId(userData.getId());
        pharmacistRepository.save(pharmacist);
        return userData;
    }

    /**
     * Retrieves all the prescriptions from the prescription API with PENDING status
     * @return - the list of pending prescriptions
     */
    public Flux<PrescriptionDTO> getPendingPrescriptions() {
        return webClient.get()
                .uri(PRESCRIPTION_API_URL + "/getPendingPrescriptions")
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToFlux(PrescriptionDTO.class);
    }

    /**
     * Sends the updated pharmacist information to the user API
     * @param pharmacist - the Pharmacist payload
     * @param pharmacistId - the ID of the pharmacist
     * @return - the UserDTO object containing the updated information
     */
    public Mono<UserDTO> updateUserInfo(Pharmacist pharmacist, Long pharmacistId) {
        UserDTO userDTO = updatePharmacistInfo(pharmacist, pharmacistId);

        return webClient.patch()
                .uri(USER_API_URL + "/updateUserInfo")
                .body(Mono.just(userDTO), UserDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(UserDTO.class);
    }

    /**
     * Sets an APPROVED or DENIED status for a PrescriptionStatusDTO object and sends it to the prescriptionAPI
     * @param status - the new status of the prescription
     * @param prescriptionId - the ID of the prescription
     * @return - the updated prescription
     */
    public Mono<PrescriptionStatusDTO> approvePrescription(PrescriptionStatusDTO status, Long prescriptionId) {
        PrescriptionStatusDTO prescriptionStatus = new PrescriptionStatusDTO();
        prescriptionStatus.setPrescriptionStatus(status.getPrescriptionStatus());

        return webClient.patch()
                .uri(PRESCRIPTION_API_URL + "/approvePrescription/" + prescriptionId)
                .body(Mono.just(prescriptionStatus), PrescriptionStatusDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(PrescriptionStatusDTO.class);
    }

    /**
     * Deletes a pharmacist from the pharmacist database and sends a DELETE request to the user API to delete
     * the user with the corresponding ID
     * @param pharmacistId - the ID of the pharmacist
     * @return - the DELETE request
     */
    public Mono<Void> deleteUser(Long pharmacistId) {
        deletePharmacist(pharmacistId);

        return webClient.delete()
                .uri(USER_API_URL + "/deleteUser/" + pharmacistId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(Void.class);
    }

    /**
     * Deletes a pharmacist from the pharmacist database based on their ID
     * @param pharmacistId - the ID of the pharmacist
     */
    private void deletePharmacist(Long pharmacistId) {
        pharmacistRepository.deleteById(pharmacistId);
    }

    /**
     * Updates the information of the pharmacist via a Pharmacist payload, merges it to the pharmacist database,
     * and stores it in a UserDTO object
     * @param pharmacist - the Pharmacist payload
     * @param pharmacistId - the ID of the pharmacist
     * @return - the UserDTO object with the updated information
     */
    private UserDTO updatePharmacistInfo(Pharmacist pharmacist, Long pharmacistId) {
        Pharmacist getPharmacist = pharmacistRepository.getReferenceById(pharmacistId);
        UserDTO userDTO = new UserDTO(pharmacistId);

        if (pharmacist.getLastName() != null) {
            getPharmacist.setLastName(pharmacist.getLastName());
            userDTO.setLastName(pharmacist.getLastName());
        }

        if (pharmacist.getEmail() != null) {
            getPharmacist.setEmail(pharmacist.getEmail());
            userDTO.setEmail(pharmacist.getEmail());
        }

        if (pharmacist.getPassword() != null) {
            getPharmacist.setPassword(pharmacist.getPassword());
            userDTO.setPassword(pharmacist.getPassword());
        }

        pharmacistRepository.save(getPharmacist);

        return userDTO;
    }

    /**
     * Sends a UserDTO payload to the user API and returns the user ID
     * @param userDTO - the userDTO object
     * @return - the userDTO payload
     */
    private UserDTO sendUserData(UserDTO userDTO) {
        return webClient.post()
                .uri("http://localhost:8081/newPharmacist")
                .body(Mono.just(userDTO), UserDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(UserDTO.class)
                .block();
    }
}
