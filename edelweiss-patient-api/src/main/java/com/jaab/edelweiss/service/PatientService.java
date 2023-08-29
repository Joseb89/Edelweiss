package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PatientRepository;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.exception.PatientNotFoundException;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.rmi.ServerException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
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
    public PatientDTO getPatientById(Long id) throws PatientNotFoundException {
        Patient patient = getPatientByPatientId(id);
        PatientDTO patientDTO = new PatientDTO();
        BeanUtils.copyProperties(patient, patientDTO);
        return patientDTO;
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's first name
     * @param firstName - the first name of the patient
     * @throws PatientNotFoundException if any patients with the specified first name are not found
     * @return - the list of the patients matching the criteria
     */
    public List<PatientDTO> getPatientsByFirstName(String firstName) throws PatientNotFoundException {
        List<Patient> patients = patientRepository.getPatientsByFirstName(firstName);

        if (patients.isEmpty())
            throw new PatientNotFoundException("No patient with the specified first name found.");

        return patients.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's last name
     * @param lastName - the last name of the patient
     * @throws PatientNotFoundException if any patients with the specified last name are not found
     * @return - the list of the patients matching the criteria
     */
    public List<PatientDTO> getPatientsByLastName(String lastName) throws PatientNotFoundException {
        List<Patient> patients = patientRepository.getPatientsByLastName(lastName);

        if (patients.isEmpty())
            throw new PatientNotFoundException("No patient with the specified last name found.");

        return patients.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's blood type
     * @param bloodType - the blood type of the patient
     * @throws PatientNotFoundException if any patients with the specified blood type are not found
     * @return - the list of the patients matching the criteria
     */
    public List<PatientDTO> getPatientsByBloodType(String bloodType) throws PatientNotFoundException {
        List<Patient> patients = patientRepository.getPatientsByBloodType(bloodType);

        if (patients.isEmpty())
            throw new PatientNotFoundException("No patient with the specified blood type found.");

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
        Patient patient = getPatientByPatientId(patientId);
        AddressDTO addressDTO = new AddressDTO();
        BeanUtils.copyProperties(patient.getAddress(), addressDTO);
        return addressDTO;
    }

    /**
     * Updates the patient's address and merges it to the address database
     * @param address - the Address payload
     * @param patientId - the ID of the patient
     * @return - the AddressDTO object containing the updated address
     */
    public AddressDTO updateAddress(Address address, Long patientId) {
        Patient patient = getPatientByPatientId(patientId);
        AddressDTO addressDTO = new AddressDTO();
        address.setId(patient.getId());
        BeanUtils.copyProperties(address, patient.getAddress());
        BeanUtils.copyProperties(patient.getAddress(), addressDTO);
        patientRepository.save(patient);
        return addressDTO;
    }

    /**
     * Updates the patient information via the Patient payload and sends it to the user API if the UserDTO
     * object containing the updated information is not null
     * @param patient - the Patient payload
     * @param patientId - the ID of the patient
     * @return - the UserDTO object containing the updated information
     */
    public Mono<UserDTO> updateUserInfo(Patient patient, Long patientId) {
        UserDTO updatedUser = updatePatientInfo(patient, patientId);

        if (updatedUser == null)
            return Mono.just(new UserDTO());

        return webClient.patch()
                .uri("/updateUserInfo")
                .body(Mono.just(updatedUser), UserDTO.class)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(UserDTO.class);
    }

    /**
     * Deletes a patient from the patient database and sends a request to the user API to delete the user with
     * the corresponding ID
     * @param patientId - the ID of the patient
     * @return - the delete request
     */
    public Mono<Void> deleteUser(Long patientId) {
        deletePatient(patientId);

        return webClient.delete()
                .uri("/deleteUser/" + patientId)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        response -> response.bodyToMono(String.class).map(Exception::new))
                .onStatus(HttpStatusCode::is5xxServerError,
                        response -> response.bodyToMono(String.class).map(ServerException::new))
                .bodyToMono(Void.class);
    }

    /**
     * Retrieves a patient from the patient database based on their ID and throws an exception if
     * the specified patient is not found
     * @param patientId - the ID of the patient
     * @throws PatientNotFoundException if the patient with the specified ID is not found
     * @return - the patient if available
     */
    private Patient getPatientByPatientId(Long patientId) throws PatientNotFoundException {
        Optional<Patient> patient = patientRepository.getPatientById(patientId);

        if (patient.isEmpty())
            throw new PatientNotFoundException("No patient with the specified ID found.");

        return patient.get();
    }

    /**
     * Deletes a patient from the patient database and their corresponding address based on the patient's ID
     * @param patientId - the ID of the patient
     */
    private void deletePatient(Long patientId) {
        Patient patient = getPatientByPatientId(patientId);

        patientRepository.deleteById(patient.getId());
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

        updatePatientIfNotNull(patient.getLastName(), getPatient::getLastName,
                getPatient::setLastName, userDTO::setLastName);

        updatePatientIfNotNull(patient.getEmail(), getPatient::getEmail,
                getPatient::setEmail, userDTO::setEmail);

        updatePatientIfNotNull(patient.getPassword(), getPatient::getPassword,
                getPatient::setPassword, userDTO::setPassword);

        updatePatientIfNotNull(patient.getPhoneNumber(), getPatient::getPhoneNumber,
                getPatient::setPhoneNumber);

        updatePatientIfNotNull(patient.getPrimaryDoctor(), getPatient::getPrimaryDoctor,
                getPatient::setPrimaryDoctor);

        patientRepository.save(getPatient);

        if (userDTOIsNull(userDTO))
            return null;

        return userDTO;
    }

    /**
     * Checks if an entity attribute is not null and does not equal the current entity value.
     * If so, it sets the attribute of the entity to the checked attribute
     * @param attribute - the attribute to check and set if it's not null
     * @param supplier - the value of the entity to check if it's equal to the new value
     * @param entity - the entity(s) to update
     * @param <T> - the type of the attribute to check
     */
    @SafeVarargs
    private <T> void updatePatientIfNotNull(T attribute, Supplier<T> supplier, Consumer<T>... entity) {
        Predicate<T> predicate = input -> !input.equals(attribute);

        if (attribute != null && predicate.test(supplier.get()))
            Arrays.stream(entity).forEach(c -> c.accept(attribute));
    }

    /**
     * Checks to see if the values of the UserDTO object are null
     * @param userDTO - the UserDTO object to check
     * @return - true if specified values are null
     */
    private boolean userDTOIsNull(UserDTO userDTO) {
        return userDTO.getLastName() == null &&
                userDTO.getEmail() == null &&
                userDTO.getPassword() == null;
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
