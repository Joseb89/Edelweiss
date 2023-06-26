package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.AppointmentRepository;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.model.Appointment;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AppointmentService {

    private AppointmentRepository appointmentRepository;

    @Autowired
    public void setAppointmentRepository(AppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    /**
     * Creates a new appointment based on appointmentDTO from the doctor API
     * @param appointmentDTO - the AppointmentDTO object from the doctor API
     * @return - appointment data
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
     * @return - Set of appointments
     */
    public Set<AppointmentDTO> getAppointmentsByDoctorName (String firstName, String lastName) {
        Set<Appointment> appointments = appointmentRepository.getAppointmentsByDoctorName(firstName, lastName);

        return appointments.stream()
                .map(this::copyToDTO)
                .collect(Collectors.toSet());
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
