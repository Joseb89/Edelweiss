package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.AppointmentRepository;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentNotFoundException;
import com.jaab.edelweiss.model.Appointment;
import com.jaab.edelweiss.utils.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AppointmentServiceTest {

    @InjectMocks
    private AppointmentService appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Test
    public void createAppointmentTest() {
        Appointment mayAppointment = TestUtils.mayAppointment;

        AppointmentDTO appointmentDTO = new AppointmentDTO(mayAppointment);

        AppointmentDTO newAppointment = appointmentService.createAppointment(appointmentDTO);

        assertEquals("Squall", newAppointment.patientFirstName());
        assertEquals("Leonheart", newAppointment.patientLastName());
    }

    @Test
    public void getAppointmentsByDoctorNameTest() {
        when(appointmentRepository.getAppointmentsByDoctorName(anyString(), anyString()))
                .thenReturn(TestUtils.getAppointmentsByDoctorName());

        List<AppointmentDTO> appointments =
                appointmentService.getAppointmentsByDoctorName(TestUtils.doctorFirstName, TestUtils.doctorLastName);

        assertEquals(2, appointments.size());
    }

    @Test
    public void updateAppointmentInfoTest() {
        Appointment appointment = TestUtils.juneAppointment;

        assertEquals(LocalDate.of(TestUtils.YEAR, 6, 6), appointment.getAppointmentDate());
        assertEquals(LocalTime.of(10, 30), appointment.getAppointmentTime());

        AppointmentDTO appointmentDTO = new AppointmentDTO(appointment.getId(), null, null,
                null, null, LocalDate.of(TestUtils.YEAR, 6, 20),
                LocalTime.of(11, 30));

        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        appointmentService.updateAppointmentInfo(appointmentDTO, appointment.getId());

        assertEquals(LocalDate.of(TestUtils.YEAR, 6, 20), appointment.getAppointmentDate());
        assertEquals(LocalTime.of(11, 30), appointment.getAppointmentTime());
    }

    @Test
    public void updateAppointmentInfoExceptionTest() {
        Appointment appointment = TestUtils.juneAppointment;

        AppointmentDTO appointmentDTO = new AppointmentDTO(appointment.getId(), null, null,
                null, null, LocalDate.of(TestUtils.YEAR, 6, 20),
                LocalTime.of(11, 30));

        assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.updateAppointmentInfo(appointmentDTO, appointment.getId()));
    }

    @Test
    public void deleteAppointmentTest() {
        Appointment appointment = TestUtils.julyAppointment;

        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(appointment));

        appointmentService.deleteAppointment(appointment.getId());

        verify(appointmentRepository, times(1)).deleteById(appointment.getId());
    }

    @Test
    public void deleteAppointmentExceptionTest() {
        assertThrows(AppointmentNotFoundException.class, () -> appointmentService.deleteAppointment(1L));
    }
}
