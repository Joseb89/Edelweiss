package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.AppointmentRepository;
import com.jaab.edelweiss.dto.AppointmentDTO;
import com.jaab.edelweiss.exception.AppointmentNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static com.jaab.edelweiss.utils.TestUtils.*;
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
        AppointmentDTO appointmentDTO = new AppointmentDTO(mayAppointment);

        AppointmentDTO newAppointment = appointmentService.createAppointment(appointmentDTO);

        assertEquals("Squall", newAppointment.patientFirstName());
        assertEquals("Leonheart", newAppointment.patientLastName());
    }

    @Test
    public void getAppointmentsByDoctorNameTest() {
        when(appointmentRepository
                .findByDoctorFirstNameAndDoctorLastNameOrderByAppointmentDate(anyString(), anyString()))
                .thenReturn(getAppointmentsByDoctorName());

        List<AppointmentDTO> appointments =
                appointmentService.getAppointmentsByDoctorName(doctorFirstName, doctorLastName);

        assertEquals(2, appointments.size());
    }

    @Test
    public void updateAppointmentInfoTest() {
        assertEquals(LocalDate.of(YEAR, 6, 6), juneAppointment.getAppointmentDate());
        assertEquals(LocalTime.of(10, 30), juneAppointment.getAppointmentTime());

        AppointmentDTO appointmentDTO = new AppointmentDTO(null, null, null,
                null, null, LocalDate.of(YEAR, 6, 20),
                LocalTime.of(11, 30));

        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(juneAppointment));

        appointmentService.updateAppointmentInfo(appointmentDTO, juneAppointment.getId());

        assertEquals(LocalDate.of(YEAR, 6, 20), juneAppointment.getAppointmentDate());
        assertEquals(LocalTime.of(11, 30), juneAppointment.getAppointmentTime());
    }

    @Test
    public void updateAppointmentInfoExceptionTest() {
        AppointmentDTO appointmentDTO = new AppointmentDTO(null, null, null,
                null, null, LocalDate.of(YEAR, 6, 20),
                LocalTime.of(11, 30));

        assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.updateAppointmentInfo(appointmentDTO, juneAppointment.getId()));
    }

    @Test
    public void deleteAppointmentTest() {
        when(appointmentRepository.findById(anyLong())).thenReturn(Optional.of(julyAppointment));

        appointmentService.deleteAppointment(julyAppointment.getId());

        verify(appointmentRepository, times(1)).deleteById(julyAppointment.getId());
    }

    @Test
    public void deleteAppointmentExceptionTest() {
        assertThrows(AppointmentNotFoundException.class,
                () -> appointmentService.deleteAppointment(julyAppointment.getId()));
    }
}
