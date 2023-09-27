package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PatientRepository;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.exception.PatientNotFoundException;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.model.Role;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Saves a new patient to the patient database and the patient's address to the address database
     *
     * @param patient - the Patient payload
     * @return - the new Patient
     */
    public PatientDTO createPatient(Patient patient) {
        Address address = new Address();
        BeanUtils.copyProperties(patient.getAddress(), address);
        address.setPatient(patient);
        patient.setAddress(address);
        patient.setRole(Role.PATIENT);

        patientRepository.save(patient);

        return new PatientDTO(patient);
    }

    /**
     * Retrieves a patient from the patient database based on the patient ID
     *
     * @param patientId - the ID of the patient
     * @return - the patient with the corresponding ID
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
        List<Patient> patients = patientRepository.getPatientsByFirstName(firstName);

        if (patients.isEmpty())
            throw new PatientNotFoundException("No patient with the specified first name found.");

        return patients.stream()
                .map(PatientDTO::new)
                .toList();
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's last name
     *
     * @param lastName - the last name of the patient
     * @return - the list of the patients matching the criteria
     * @throws PatientNotFoundException if any patients with the specified last name are not found
     */
    public List<PatientDTO> getPatientsByLastName(String lastName) throws PatientNotFoundException {
        List<Patient> patients = patientRepository.getPatientsByLastName(lastName);

        if (patients.isEmpty())
            throw new PatientNotFoundException("No patient with the specified last name found.");

        return patients.stream()
                .map(PatientDTO::new)
                .toList();
    }

    /**
     * Retrieves a list of patients from the patient database based on the patient's blood type
     *
     * @param bloodType - the blood type of the patient
     * @return - the list of the patients matching the criteria
     * @throws PatientNotFoundException if any patients with the specified blood type are not found
     */
    public List<PatientDTO> getPatientsByBloodType(String bloodType) throws PatientNotFoundException {
        List<Patient> patients = patientRepository.getPatientsByBloodType(bloodType);

        if (patients.isEmpty())
            throw new PatientNotFoundException("No patient with the specified blood type found.");

        return patients.stream()
                .map(PatientDTO::new)
                .toList();
    }

    /**
     * Retrieves a patient's address from the address database and saves it to an AddressDTO object
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
     * Updates the patient's address via an Address payload and merges it to the address database
     *
     * @param patientId - the ID of the patient
     * @param fields    - the Address payload
     * @return - the updated address
     */
    public AddressDTO updateAddress(Long patientId, Map<String, Object> fields) {
        Patient patient = getPatientByPatientId(patientId);

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
     * Updates the information of the patient via a Patient payload and merges it to the patient database
     *
     * @param patientId - the ID of the patient
     * @param fields    - the Patient payload
     * @return - the updated patient information
     */
    public PatientDTO updatePatientInfo(Long patientId, Map<String, Object> fields) {
        Patient patient = getPatientByPatientId(patientId);

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Patient.class, key);

            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, patient, value);
            }
        });

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
     * Retrieves a patient from the patient database based on their ID and throws an exception if
     * the specified patient is not found
     *
     * @param patientId - the ID of the patient
     * @return - the patient if available
     * @throws PatientNotFoundException if the patient with the specified ID is not found
     */
    private Patient getPatientByPatientId(Long patientId) throws PatientNotFoundException {
        Optional<Patient> patient = patientRepository.findById(patientId);

        if (patient.isEmpty())
            throw new PatientNotFoundException("No patient with the specified ID found.");

        return patient.get();
    }
}
