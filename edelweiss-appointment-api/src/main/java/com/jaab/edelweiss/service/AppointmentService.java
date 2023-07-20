package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.AppointmentRepository;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentNotFoundException;
import com.jaab.edelweiss.model.Appointment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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
        AppointmentDTO getAppointment = new AppointmentDTO();
        getAppointment.setId(appointmentId);

        if (appointmentDTO.getPatientFirstName() != null)
            appointment.setPatientFirstName(appointmentDTO.getPatientFirstName());

        if (appointmentDTO.getPatientLastName() != null)
            appointment.setPatientLastName(appointmentDTO.getPatientLastName());

        if (appointmentDTO.getAppointmentDate() != null)
            appointment.setAppointmentDate(appointmentDTO.getAppointmentDate());

        if (appointmentDTO.getAppointmentTime() != null)
            appointment.setAppointmentTime(appointmentDTO.getAppointmentTime());

        appointmentRepository.save(appointment);

        BeanUtils.copyProperties(appointment, getAppointment);

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
