package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.AppointmentRepository;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentException;
import com.jaab.edelweiss.exception.AppointmentNotFoundException;
import com.jaab.edelweiss.model.Appointment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;

    @Autowired
    public void setAppointmentRepository(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Creates a new appointment based on an AppointmentDTO from the doctor API
     * @param appointmentDTO - the AppointmentDTO object from the doctor API
     * @return - the appointment data
     */
    public Appointment createAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(appointmentDTO, appointment);
        appointmentRepository.save(appointment);

        return appointment;
    }

    /**
     * Retrieves a list of appointments from the appointment database based on the doctor's name
     * @param firstName - the first name of the doctor
     * @param lastName - the last name of the doctor
     * @return - the List of appointments
     */
    public List<AppointmentDTO> getAppointmentsByDoctorName (String firstName, String lastName) {
        List<Appointment> appointments = appointmentRepository.getAppointmentsByDoctorName(firstName, lastName);

        return appointments.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Updates the appointment with the corresponding ID and merges it to the appointment database
     * @param appointmentDTO - the AppointmentDTO payload from the doctor API
     * @param appointmentId - the ID of the appointment
     * @return - the updated appointment
     */
    public AppointmentDTO updateAppointmentInfo(AppointmentDTO appointmentDTO, Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);

        updateAppointmentIfNotNull(appointmentDTO.getPatientFirstName(), appointment::getPatientFirstName,
                appointment::setPatientFirstName);

        updateAppointmentIfNotNull(appointmentDTO.getPatientLastName(), appointment::getPatientLastName,
                appointment::setPatientLastName);

        updateAppointmentIfNotNull(appointmentDTO.getAppointmentDate(), appointment::getAppointmentDate,
                appointment::setAppointmentDate);

        updateAppointmentIfNotNull(appointmentDTO.getAppointmentTime(), appointment::getAppointmentTime,
                appointment::setAppointmentTime);

        if (appointment.getAppointmentDate().isBefore(LocalDate.now()) &&
                appointment.getAppointmentTime().isBefore(LocalTime.now()))
            throw new AppointmentException("Appointment date must be today or later date.");

        appointmentRepository.save(appointment);

        AppointmentDTO getAppointment = new AppointmentDTO();

        BeanUtils.copyProperties(appointment, getAppointment);
        getAppointment.setId(appointmentId);

        return getAppointment;
    }

    /**
     * Deletes an appointment from the appointment database based on their ID
     * @param appointmentId - the ID of the appointment
     */
    public void deleteAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);

        appointmentRepository.deleteById(appointment.getId());
    }

    private <T> void updateAppointmentIfNotNull(T attribute, Supplier<T> supplier, Consumer<T> entity) {
        Predicate<T> predicate = input -> !input.equals(attribute);

        if (attribute != null && predicate.test(supplier.get()))
            entity.accept(attribute);
    }

    /**
     * Retrieves an appointment from the appointment database based on their ID and throws an exception if the
     * specified appointment is not found
     * @param appointmentId - the ID of the appointment
     * @return - the appointment if available
     */
    private Appointment getAppointmentById(Long appointmentId) {
        Optional<Appointment> appointment = appointmentRepository.getAppointmentById(appointmentId);

        if (appointment.isEmpty())
            throw new AppointmentNotFoundException("No appointment with the specified ID found.");

        return appointment.get();
    }

    /**
     * Copies the values of an Appointment object into a AppointmentDTO object
     * @param appointment - the Appointment object
     * @return - the AppointmentDTO object
     */
    private AppointmentDTO copyToDTO(Appointment appointment) {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        BeanUtils.copyProperties(appointment, appointmentDTO);
        return appointmentDTO;
    }
}
