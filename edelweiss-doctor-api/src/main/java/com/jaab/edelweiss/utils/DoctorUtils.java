package com.jaab.edelweiss.utils;

import com.jaab.edelweiss.dao.DoctorRepository;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.model.Doctor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DoctorUtils {

    private final DoctorRepository doctorRepository;

    public DoctorUtils(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    /**
     * Retrieves a doctor from the doctor database based on the doctor's ID and sets the doctor's
     * first and last name to a String array
     * @param physicianId - the ID of the doctor
     * @return - the String array containing the doctor's first and last name
     */
    public String[] setDoctorName(Long physicianId) {
        Doctor doctor = doctorRepository.getReferenceById(physicianId);
        String[] doctorName = new String[2];

        doctorName[0] = doctor.getFirstName();
        doctorName[1] = doctor.getLastName();

        return doctorName;
    }

    public boolean prescriptionNameIsNotValid(PrescriptionDTO prescriptionDTO) {
        return prescriptionDTO.getPrescriptionName().isEmpty();
    }

    public boolean prescriptionNameIsNotValid(UpdatePrescriptionDTO prescriptionDTO) {
        return prescriptionDTO.getPrescriptionName().isEmpty();
    }

    public boolean prescriptionDosageIsNotValid(PrescriptionDTO prescriptionDTO) {
        return prescriptionDTO.getPrescriptionDosage() < 1;
    }

    public boolean prescriptionDosageIsNotValid(UpdatePrescriptionDTO prescriptionDTO) {
        return prescriptionDTO.getPrescriptionDosage() < 1;
    }

    public boolean appointmentDateIsNotValid(AppointmentDTO appointmentDTO) {
        return appointmentDTO.getAppointmentDate().isBefore(LocalDate.now());
    }
}
