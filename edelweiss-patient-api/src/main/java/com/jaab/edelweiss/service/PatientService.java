package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PatientRepository;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;
import java.util.List;
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
     * Saves a new patient to the patient database and the patient's address to the address database
     * @param patient - the new Patient
     * @return - the UserDTO payload
     */
    public UserDTO createPatient(Patient patient) {
        UserDTO userDTO = new UserDTO();
        Address address = new Address();
        BeanUtils.copyProperties(patient, userDTO);
        UserDTO userData = sendUserData(userDTO);
        patient.setId(userData.getId());
        BeanUtils.copyProperties(patient.getAddress(), address);
        address.setPatient(patient);
        patient.setAddress(address);
        patientRepository.save(patient);
        return userData;
    }

    /**
     * Retrieves a patient from the patient database based on the patient ID
     * @param id - the ID of the patient
     * @return - the patient with the corresponding ID
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
     * @return - the list of the patients matching the criteria
     */
    public List<PatientDTO> getPatientsByFirstName(String firstName) {
        List<Patient> patients = patientRepository.getPatientsByFirstName(firstName);

        return patients.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's last name
     * @param lastName - the last name of the patient
     * @return - the list of the patients matching the criteria
     */
    public List<PatientDTO> getPatientsByLastName(String lastName) {
        List<Patient> patients = patientRepository.getPatientsByLastName(lastName);

        return patients.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's blood type
     * @param bloodType - the blood type of the patient
     * @return - the list of the patients matching the criteria
     */
    public List<PatientDTO> getPatientsByBloodType(String bloodType) {
        List<Patient> patients =patientRepository.getPatientsByBloodType(bloodType);

        return patients.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a patient's address from the address database and saves it to an AddressDTO object
     * @param patientId - the ID of the patient
     * @return - the AddressDTO object with the address information
     */
    public AddressDTO getAddress(Long patientId) {
        Patient patient = patientRepository.getReferenceById(patientId);
        Address address = patient.getAddress();
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(address, addressDTO);
        return addressDTO;
    }

    /**
     * Updates the patient's address and merges it to the address database
     * @param address - the Address payload
     * @param patientId - the ID of the patient
     * @return - the AddressDTO object containing the updated address
     */
    public AddressDTO updateAddress(Address address, Long patientId) {
        AddressDTO addressDTO = new AddressDTO();
        Patient patient = patientRepository.getReferenceById(patientId);
        address.setId(patient.getId());
        BeanUtils.copyProperties(address, patient.getAddress());
        BeanUtils.copyProperties(patient.getAddress(), addressDTO);
        patientRepository.save(patient);
        return addressDTO;
    }

    /**
     * Sends the updated patient information to the user API
     * @param patient - the Patient payload
     * @param patientId - the ID of the patient
     * @return - the UserDTO object containing the updated information
     */
    public Mono<UserDTO> updateUserInfo(Patient patient, Long patientId) {
        UserDTO userDTO = updatePatientInfo(patient, patientId);

        return webClient.patch()
                .uri("/updateUserInfo")
                .body(Mono.just(userDTO), UserDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(UserDTO.class);
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
     * Updates the information of the patient via a Patient payload, merges it to the patient database,
     * and stores it in a UserDTO object
     * @param patient - the Patient payload
     * @param patientId - the ID of the patient
     * @return - the UserDTO object with the updated information
     */
    private UserDTO updatePatientInfo(Patient patient, Long patientId) {
        Patient getPatient = patientRepository.getReferenceById(patientId);
        UserDTO userDTO = new UserDTO(patientId);

        if (patient.getLastName() != null) {
            getPatient.setLastName(patient.getLastName());
            userDTO.setLastName(getPatient.getLastName());
        }

        if (patient.getEmail() != null) {
            getPatient.setEmail(patient.getEmail());
            userDTO.setEmail(getPatient.getEmail());
        }

        if (patient.getPassword() != null) {
            getPatient.setPassword(patient.getPassword());
            userDTO.setPassword(getPatient.getPassword());
        }

        if (patient.getPhoneNumber() != null)
            getPatient.setPhoneNumber(patient.getPhoneNumber());

        if (patient.getPrimaryDoctor() != null)
            getPatient.setPrimaryDoctor(patient.getPrimaryDoctor());

        patientRepository.save(getPatient);

        return userDTO;
    }

    /**
     * Sends a UserDTO payload to the user API and returns the user ID
     * @param userDTO - the UserDTO object
     * @return - the UserDTO payload
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
