package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.DoctorRepository;
import com.jaab.edelweiss.exception.DoctorNotFoundException;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.model.Role;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Optional;

/**
 * This class serves as a service for creating new physicians and maintaining their data
 *
 * @author Joseph Barr
 */
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    /**
     * Saves a new doctor to the doctor database
     *
     * @param doctor - the Doctor object
     * @return - the new doctor
     */
    public Doctor createDoctor(Doctor doctor) {
        doctor.setRole(Role.PHYSICIAN);
        doctorRepository.save(doctor);

        return doctor;
    }

    /**
     * Updates the information of the doctor and merges it to the doctor database
     *
     * @param physicianId - the ID of the doctor
     * @param fields      - the updated information
     * @return - the updated doctor
     */
    public Doctor updateDoctorInfo(Long physicianId, Map<String, Object> fields) {
        Doctor doctor = getDoctorById(physicianId);

        fields.forEach((key, value) -> {
            Field field = ReflectionUtils.findField(Doctor.class, key);

            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, doctor, value);
            }
        });

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
        Optional<Doctor> doctor = doctorRepository.findById(physicianId);

        if (doctor.isEmpty())
            throw new DoctorNotFoundException("No doctor with the specified ID found.");

        return doctor.get();
    }
}
