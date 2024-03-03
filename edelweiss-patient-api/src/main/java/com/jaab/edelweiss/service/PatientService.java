package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PatientRepository;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.LoginDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.exception.PatientNotFoundException;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private final PasswordEncoder passwordEncoder;

    public PatientService(PatientRepository patientRepository, PasswordEncoder passwordEncoder) {
        this.patientRepository = patientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a new patient to the patient database and the patient's address to the address database
     *
     * @param patient - the Patient object
     * @return - the new patient
     */
    public PatientDTO createPatient(Patient patient) {
        patient.setPassword(passwordEncoder.encode(patient.getPassword()));

        Address address = new Address();
        BeanUtils.copyProperties(patient.getAddress(), address);

        address.setPatient(patient);
        patient.setAddress(address);

        patientRepository.save(patient);

        return new PatientDTO(patient);
    }

    /**
     * Retrieves a patient from the patient database based on the patient's ID
     *
     * @param patientId - the ID of the patient
     * @return - the patient with the specified ID
     * @throws PatientNotFoundException if the patient with the specified ID is not found
     */
    public PatientDTO getPatientById(Long patientId) throws PatientNotFoundException {
        Patient patient = getPatientByPatientId(patientId);

        return new PatientDTO(patient);
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's first name
     *
     * @param firstName - the first name of the patient
     * @return - the list of the patients matching the criteria
     * @throws PatientNotFoundException if any patients with the specified first name are not found
     */
    public List<PatientDTO> getPatientsByFirstName(String firstName) throws PatientNotFoundException {
        return getPatientData(patientRepository.findByFirstNameOrderByLastName(firstName),
                "No patients with the specified first name found.");
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's last name
     *
     * @param lastName - the last name of the patient
     * @return - the list of the patients matching the criteria
     * @throws PatientNotFoundException if any patients with the specified last name are not found
     */
    public List<PatientDTO> getPatientsByLastName(String lastName) throws PatientNotFoundException {
        return getPatientData(patientRepository.findByLastNameOrderByFirstName(lastName),
                "No patients with the specified last name found.");
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's blood type
     *
     * @param bloodType - the blood type of the patient
     * @return - the list of the patients matching the criteria
     * @throws PatientNotFoundException if any patients with the specified blood type are not found
     */
    public List<PatientDTO> getPatientsByBloodType(String bloodType) throws PatientNotFoundException {
        return getPatientData(patientRepository.findByBloodType(bloodType),
                "No patients with the specified blood type found.");
    }

    /**
     * Retrieves a patient's address from the address database and stores it in an AddressDTO object
     *
     * @param patientId - the ID of the patient
     * @return - the AddressDTO object with the address information
     * @throws PatientNotFoundException if the patient with the specified ID is not found
     */
    public AddressDTO getAddress(Long patientId) throws PatientNotFoundException {
        Patient patient = getPatientByPatientId(patientId);

        return new AddressDTO(patient.getAddress());
    }

    /**
     * Updates the patient's address and merges it to the address database
     *
     * @param fields - the object containing the updated information
     * @return - the updated address
     */
    public AddressDTO updateAddress(Map<String, Object> fields) {
        LoginDTO loginDTO = getUserDetails();

        Patient patient = getPatientByPatientId(loginDTO.id());

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Address.class, key);

            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, patient.getAddress(), value);
            }
        });

        patientRepository.save(patient);

        return new AddressDTO(patient.getAddress());
    }

    /**
     * Updates the patient's information and merges it to the patient database
     *
     * @param fields - the object containing the updated information
     * @return - the updated patient information
     */
    public PatientDTO updatePatientInfo(Map<String, Object> fields) {
        LoginDTO loginDTO = getUserDetails();

        Patient patient = getPatientByPatientId(loginDTO.id());

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Patient.class, key);

            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, patient, value);
            }
        });

        if (fields.get("password") != null)
            patient.setPassword(passwordEncoder.encode(fields.get("password").toString()));

        patientRepository.save(patient);

        return new PatientDTO(patient);
    }

    /**
     * Deletes a patient from the patient database based on the patient's ID
     *
     * @param patientId - the ID of the patient
     * @throws PatientNotFoundException if the patient with the specified ID is not found
     */
    public void deletePatient(Long patientId) throws PatientNotFoundException {
        Patient patient = getPatientByPatientId(patientId);

        patientRepository.deleteById(patient.getId());
    }

    /**
     * Retrieves a patient from the patient database based on the patient's ID
     *
     * @param patientId - the ID of the patient
     * @return - the patient if available
     * @throws PatientNotFoundException if the patient with the specified ID is not found
     */
    private Patient getPatientByPatientId(Long patientId) throws PatientNotFoundException {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new PatientNotFoundException("No patient with the specified ID found."));
    }

    /**
     * Retrieves a list of patients from the patient database and saves the data to a PatientDTO list
     *
     * @param patientList  - the list of patients from the patient database
     * @param errorMessage - the error message to display if the returned list is empty
     * @return - the PatientDTO list containing the patients
     * @throws PatientNotFoundException if the returned list of patients is empty
     */
    private List<PatientDTO> getPatientData(List<Patient> patientList, String errorMessage)
            throws PatientNotFoundException {
        if (patientList.isEmpty())
            throw new PatientNotFoundException(errorMessage);

        return patientList.stream()
                .map(PatientDTO::new)
                .toList();
    }

    /**
     * Retrieves the UserDetails information of an authenticated user
     *
     * @return - the UserDetails stored within the LoginDTO object
     */
    private LoginDTO getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        return (LoginDTO) authentication.getPrincipal();
    }
}
