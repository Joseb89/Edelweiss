package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PatientRepository;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Patient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PatientService {

    private final WebClient webClient;

    private PatientRepository patientRepository;

    @Autowired
    public PatientService(WebClient.Builder builder) {
        this.webClient = builder.baseUrl("http://localhost:8081").build();
    }

    @Autowired
    public void setPatientRepository(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Saves a new patient to the patient database
     * @param patient - the new Patient
     * @return - the user payload
     */
    public UserDTO createPatient(Patient patient) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(patient, userDTO);
        UserDTO userData = sendUserData(userDTO);
        patient.setId(userData.getId());
        patientRepository.save(patient);
        return userData;
    }

    /**
     * Retrieves patient data from the patient database based on patient ID
     * @param id - the ID of the patient
     * @return - patient data stored in PatientDTO object
     */
    public PatientDTO getPatientById(Long id) {
        PatientDTO patientDTO = new PatientDTO();
        Patient patient = patientRepository.getReferenceById(id);
        BeanUtils.copyProperties(patient, patientDTO);
        return patientDTO;
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's first name
     * @param firstName - the first name of the patient
     * @return - the set of the patients matching the criteria
     */
    public Set<PatientDTO> getPatientByFirstName(String firstName) {
        Set<Patient> patients = patientRepository.getPatientByFirstName(firstName);

        return patients.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's last name
     * @param lastName - the last name of the patient
     * @return - the set of the patients matching the criteria
     */
    public Set<PatientDTO> getPatientByLastName(String lastName) {
        Set<Patient> patients = patientRepository.getPatientsByLastName(lastName);

        return patients.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toSet());
    }

    /**
     * Copies the values of a Patient object into a PatientDTO object
     * @param patient - the Patient object
     * @return - the PatientDTO object
     */
    private PatientDTO copyToDTO(Patient patient) {
        PatientDTO patientDTO = new PatientDTO();
        BeanUtils.copyProperties(patient, patientDTO);
        return patientDTO;
    }

    /**
     * Sends a UserDTO payload to the user API and returns the user id
     * @param userDTO - the UserDTO object
     * @return - the UserDTO post payload
     */
    private UserDTO sendUserData(UserDTO userDTO) {
        return webClient.post()
                .uri("/newPatient")
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
