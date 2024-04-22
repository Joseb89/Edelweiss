package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.DoctorRepository;
import com.jaab.edelweiss.dto.LoginDTO;
import com.jaab.edelweiss.exception.DoctorNotFoundException;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.utils.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * This class is a service for creating new physicians and maintaining their data
 *
 * @author Joseph Barr
 */
@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Saves a new doctor to the doctor database
     *
     * @param doctor - the Doctor object
     * @return - the new doctor
     */
    public Doctor createDoctor(Doctor doctor) {
        doctor.setPassword(passwordEncoder.encode(doctor.getPassword()));
        doctorRepository.save(doctor);

        return doctor;
    }

    /**
     * Updates the information of the doctor and merges it to the doctor database
     *
     * @param fields - the updated information
     * @return - the updated doctor
     */
    public Doctor updateDoctorInfo(Map<String, Object> fields) {
        LoginDTO loginDTO = AuthUtils.getUserDetails();

        Doctor doctor = getDoctorById(loginDTO.id());

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Doctor.class, key);

            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, doctor, value);
            }
        });

        if (fields.get("password") != null)
            doctor.setPassword(passwordEncoder.encode(fields.get("password").toString()));

        doctorRepository.save(doctor);

        return doctor;
    }

    /**
     * Deletes a doctor from the doctor database based on the doctor's ID
     *
     * @param physicianId - the ID of the doctor
     * @throws DoctorNotFoundException if the doctor with the specified ID is not found
     */
    public void deleteDoctor(Long physicianId) throws DoctorNotFoundException {
        Doctor doctor = getDoctorById(physicianId);

        doctorRepository.deleteById(doctor.getId());
    }

    /**
     * Retrieves a doctor from the doctor database based on their ID
     *
     * @param physicianId - the ID of the doctor
     * @return - the doctor if found
     * @throws DoctorNotFoundException if the doctor with the specified ID is not found
     */
    private Doctor getDoctorById(Long physicianId) throws DoctorNotFoundException {
        return doctorRepository.findById(physicianId)
                .orElseThrow(() -> new DoctorNotFoundException("No doctor with the specified ID found."));
    }
}
