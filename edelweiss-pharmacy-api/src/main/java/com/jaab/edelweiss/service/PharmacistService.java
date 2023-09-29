package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PharmacistRepository;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.exception.PharmacistNotFoundException;
import com.jaab.edelweiss.exception.PrescriptionStatusException;
import com.jaab.edelweiss.model.Pharmacist;
import com.jaab.edelweiss.model.Role;
import com.jaab.edelweiss.model.Status;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;
import java.rmi.ServerException;
import java.util.Map;
import java.util.Optional;

@Service
public class PharmacistService {

    private final PharmacistRepository pharmacistRepository;

    private final WebClient webClient;

    public PharmacistService(PharmacistRepository pharmacistRepository, WebClient.Builder builder) {
        this.pharmacistRepository = pharmacistRepository;
        this.webClient = builder.baseUrl("http://localhost:8084/pharmacy").build();
    }

    /**
     * Saves a new pharmacist to the pharmacist database
     *
     * @param pharmacist - the Pharmacist payload
     * @return - the new Pharmacist
     */
    public Pharmacist createPharmacist(Pharmacist pharmacist) {
        pharmacist.setRole(Role.PHARMACIST);
        pharmacistRepository.save(pharmacist);

        return pharmacist;
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
     * Updates the information of the Pharmacist via a Pharmacist payload
     * and merges it to the pharmacist database
     *
     * @param pharmacistId - the ID of the pharmacist
     * @param fields       - the Pharmacist payload
     * @return - the updated Pharmacist
     */
    public Pharmacist updateUserInfo(Long pharmacistId, Map<String, Object> fields) {
        Pharmacist pharmacist = getPharmacistById(pharmacistId);

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Pharmacist.class, key);

            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, pharmacist, value);
            }
        });

        pharmacistRepository.save(pharmacist);

        return pharmacist;
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

        PrescriptionStatusDTO prescriptionStatus = new PrescriptionStatusDTO(status.prescriptionStatus());

        return webClient.patch()
                .uri("/approvePrescription/" + prescriptionId)
                .body(Mono.just(prescriptionStatus), PrescriptionStatusDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(PrescriptionStatusDTO.class);
    }

    /**
     * Deletes a pharmacist from the pharmacist database based on their ID
     *
     * @param pharmacistId - the ID of the pharmacist
     * @throws PharmacistNotFoundException if the pharmacist with the specified ID is not found
     */
    public void deletePharmacist(Long pharmacistId) throws PharmacistNotFoundException {
        Pharmacist pharmacist = getPharmacistById(pharmacistId);

        pharmacistRepository.deleteById(pharmacist.getId());
    }

    /**
     * Retrieves a pharmacist from the pharmacist database based on their ID
     *
     * @param pharmacistId - the ID of the pharmacist
     * @return - the pharmacist if available
     * @throws PharmacistNotFoundException if the pharmacist with the specified ID is not found
     */
    private Pharmacist getPharmacistById(Long pharmacistId) throws PharmacistNotFoundException {
        Optional<Pharmacist> pharmacist = pharmacistRepository.findById(pharmacistId);

        if (pharmacist.isEmpty())
            throw new PharmacistNotFoundException("No pharmacist with the specified ID found.");

        return pharmacist.get();
    }
}
