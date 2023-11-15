package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.AppointmentRepository;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentNotFoundException;
import com.jaab.edelweiss.model.Appointment;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    public AppointmentService(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Creates a new appointment based on an AppointmentDTO object from the doctor API
     *
     * @param appointmentDTO - the AppointmentDTO object from the doctor API
     * @return - the appointment data
     */
    public AppointmentDTO createAppointment(AppointmentDTO appointmentDTO) {
        Appointment appointment = new Appointment();
        BeanUtils.copyProperties(appointmentDTO, appointment);

        appointmentRepository.save(appointment);

        return new AppointmentDTO(appointment);
    }

    /**
     * Retrieves a list of appointments from the appointment database based on the doctor's name
     *
     * @param firstName - the first name of the doctor
     * @param lastName  - the last name of the doctor
     * @return - the list of appointments
     */
    public List<AppointmentDTO> getAppointmentsByDoctorName(String firstName, String lastName) {
        List<Appointment> appointments = appointmentRepository.getAppointmentsByDoctorName(firstName, lastName);

        return appointments.stream()
                .map(AppointmentDTO::new)
                .toList();
    }

    /**
     * Updates the appointment with the specified ID and merges it to the appointment database
     *
     * @param appointmentDTO - the AppointmentDTO object from the doctor API
     * @param appointmentId  - the ID of the appointment
     * @return - the updated appointment
     * @throws AppointmentNotFoundException if the appointment with the specified ID is not found
     */
    public AppointmentDTO updateAppointmentInfo(AppointmentDTO appointmentDTO, Long appointmentId)
            throws AppointmentNotFoundException {
        Appointment appointment = getAppointmentById(appointmentId);

        updateAppointmentIfNotNull(appointmentDTO.patientFirstName(), appointment::getPatientFirstName,
                appointment::setPatientFirstName);

        updateAppointmentIfNotNull(appointmentDTO.patientLastName(), appointment::getPatientLastName,
                appointment::setPatientLastName);

        updateAppointmentIfNotNull(appointmentDTO.appointmentDate(), appointment::getAppointmentDate,
                appointment::setAppointmentDate);

        updateAppointmentIfNotNull(appointmentDTO.appointmentTime(), appointment::getAppointmentTime,
                appointment::setAppointmentTime);

        appointmentRepository.save(appointment);

        return new AppointmentDTO(appointment);
    }

    /**
     * Deletes an appointment from the appointment database based on their ID
     *
     * @param appointmentId - the ID of the appointment
     * @throws AppointmentNotFoundException if the appointment with the specified ID is not found
     */
    public void deleteAppointment(Long appointmentId) throws AppointmentNotFoundException {
        Appointment appointment = getAppointmentById(appointmentId);

        appointmentRepository.deleteById(appointment.getId());
    }

    /**
     * Checks the fields of an Appointment payload object and updates the specified object if the specified field
     * is not null and does not equal the current value of the field
     *
     * @param attribute           - the field attribute to check
     * @param appointmentSupplier - the Appointment entity to check
     * @param appointmentConsumer - the Appointment entity to update if the requirements are met
     * @param <T>                 - the type of the attribute
     */
    private <T> void updateAppointmentIfNotNull(T attribute, Supplier<T> appointmentSupplier,
                                                Consumer<T> appointmentConsumer) {
        Predicate<T> predicate = input -> !input.equals(attribute);

        if (attribute != null && predicate.test(appointmentSupplier.get()))
            appointmentConsumer.accept(attribute);
    }

    /**
     * Retrieves an appointment from the appointment database based on its ID
     *
     * @param appointmentId - the ID of the appointment
     * @return - the appointment if available
     * @throws AppointmentNotFoundException if the appointment with the specified ID is not found
     */
    private Appointment getAppointmentById(Long appointmentId) throws AppointmentNotFoundException {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(()-> new AppointmentNotFoundException("No appointment with the specified ID found."));
    }
}
