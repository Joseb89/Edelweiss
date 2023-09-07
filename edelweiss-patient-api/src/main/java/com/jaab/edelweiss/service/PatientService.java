package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PatientRepository;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.exception.PatientNotFoundException;
import com.jaab.edelweiss.model.Address;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.model.Role;
import com.jaab.edelweiss.utils.PatientUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    private final PatientUtils patientUtils;

    public PatientService(PatientRepository patientRepository, PatientUtils patientUtils) {
        this.patientRepository = patientRepository;
        this.patientUtils = patientUtils;
    }

    /**
     * Saves a new patient to the patient database and the patient's address to the address database
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
     * @param patientId - the ID of the patient
     * @throws PatientNotFoundException if the patient with the specified ID is not found
     * @return - the patient with the corresponding ID
     */
    public PatientDTO getPatientById(Long patientId) throws PatientNotFoundException {
        Patient patient = patientUtils.getPatientById(patientId);
        return new PatientDTO(patient);
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
                .map(PatientDTO::new)
                .toList();
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
                .map(PatientDTO::new)
                .toList();
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
                .map(PatientDTO::new)
                .toList();
    }

    /**
     * Retrieves a patient's address from the address database and saves it to an AddressDTO object
     * @param patientId - the ID of the patient
     * @throws PatientNotFoundException if the patient with the specified ID is not found
     * @return - the AddressDTO object with the address information
     */
    public AddressDTO getAddress(Long patientId) throws PatientNotFoundException {
        Patient patient = patientUtils.getPatientById(patientId);
        return new AddressDTO(patient.getAddress());
    }

    /**
     * Updates the patient's address via an Address payload and merges it to the address database
     * @param patientId - the ID of the patient
     * @param fields - the Address payload
     */
    public void updateAddress(Long patientId, Map<String, Object> fields) {
        Optional<Patient> patient = patientRepository.findById(patientId);

        if (patient.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Address.class, key);

                if (Objects.nonNull(field)) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, patient.get().getAddress(), value);
                }
            });

            patientRepository.save(patient.get());
        }
    }

    /**
     * Updates the information of the patient via a Patient payload and merges it to the patient database
     * @param patientId - the ID of the patient
     * @param fields - the Patient payload
     */
    public void updatePatientInfo(Long patientId, Map<String, Object> fields) {
        Optional<Patient> patient = patientRepository.findById(patientId);

        if (patient.isPresent()) {
            fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(Patient.class, key);

                if (Objects.nonNull(field)) {
                    field.setAccessible(true);
                    ReflectionUtils.setField(field, patient.get(), value);
                }
            });

            patientRepository.save(patient.get());
        }
    }

    /**
     * Deletes a patient from the patient database based on the patient's ID
     * @param patientId - the ID of the patient
     * @throws PatientNotFoundException if the patient with the specified ID is not found
     */
    public void deletePatient(Long patientId) throws PatientNotFoundException {
        Patient patient = patientUtils.getPatientById(patientId);

        patientRepository.deleteById(patient.getId());
    }
}
