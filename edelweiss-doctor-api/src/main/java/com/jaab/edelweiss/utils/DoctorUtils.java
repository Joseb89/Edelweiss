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
     *
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

    /**
     * Checks to see if a prescription name is null or empty
     *
     * @param prescriptionDTO - the PrescriptionDTO object containing the prescription name
     * @return - true if the prescription name is null or empty
     */
    public boolean prescriptionNameIsNotValid(PrescriptionDTO prescriptionDTO) {
        return prescriptionDTO.getPrescriptionName() == null ||
                prescriptionDTO.getPrescriptionName().isEmpty();
    }

    /**
     * Checks to see if a prescription name is empty
     *
     * @param prescriptionDTO - the UpdatePrescriptionDTO object containing the prescription name
     * @return - true if the prescription name is empty
     */
    public boolean prescriptionNameIsNotValid(UpdatePrescriptionDTO prescriptionDTO) {
        return prescriptionDTO.getPrescriptionName().isEmpty();
    }

    /**
     * Checks to see if a prescription dosage is null or less than 1cc
     *
     * @param prescriptionDTO - the PrescriptionDTO object containing the prescription dosage
     * @return - true if the prescription dosage is null or less than 1cc
     */
    public boolean prescriptionDosageIsNotValid(PrescriptionDTO prescriptionDTO) {
        return prescriptionDTO.getPrescriptionDosage() == null ||
                prescriptionDTO.getPrescriptionDosage() < 1;
    }

    /**
     * Checks to see if a prescription dosage is less than 1cc
     *
     * @param prescriptionDTO - the UpdatePrescriptionDTO object containing the prescription dosage
     * @return - true if the prescription dosage is less than 1cc
     */
    public boolean prescriptionDosageIsNotValid(UpdatePrescriptionDTO prescriptionDTO) {
        return prescriptionDTO.getPrescriptionDosage() < 1;
    }

    /**
     * Checks to see if an appointment date is null or before the current date
     *
     * @param appointmentDTO - the AppointmentDTO object containing the appointment date
     * @return - true if the appointment date is null or before the current date
     */
    public boolean appointmentDateIsNotValid(AppointmentDTO appointmentDTO) {
        return appointmentDTO.getAppointmentDate() == null ||
                appointmentDTO.getAppointmentDate().isBefore(LocalDate.now());
    }
}
