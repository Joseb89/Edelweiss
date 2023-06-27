package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PharmacistRepository;
import com.jaab.edelweiss.dto.PrescriptionDTO;
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
     * @param pharmacist - the new pharmacist
     * @return - the userDTO payload
     */
    public UserDTO createPharmacist(Pharmacist pharmacist) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(pharmacist, userDTO);
        UserDTO userData = sendUserData(userDTO);
        pharmacist.setId(userData.getId());
        pharmacistRepository.save(pharmacist);
        return userData;
    }

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
     * Sends UserDTO object to the user API
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
