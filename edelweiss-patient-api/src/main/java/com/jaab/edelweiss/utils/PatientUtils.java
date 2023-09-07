package com.jaab.edelweiss.utils;

import com.jaab.edelweiss.dao.PatientRepository;
import com.jaab.edelweiss.exception.PatientNotFoundException;
import com.jaab.edelweiss.model.Patient;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PatientUtils {

    private final PatientRepository patientRepository;

    public PatientUtils(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Retrieves a patient from the patient database based on their ID and throws an exception if
     * the specified patient is not found
     * @param patientId - the ID of the patient
     * @throws PatientNotFoundException if the patient with the specified ID is not found
     * @return - the patient if available
     */
    public Patient getPatientById(Long patientId) throws PatientNotFoundException {
        Optional<Patient> patient = patientRepository.findById(patientId);

        if (patient.isEmpty())
            throw new PatientNotFoundException("No patient with the specified ID found.");

        return patient.get();
    }
}
