package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.DoctorRepository;
import com.jaab.edelweiss.exception.DoctorNotFoundException;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorRepository  doctorRepository;

    private Doctor doctor;

    @BeforeEach
    public void init() {
        assertNotNull(doctorService);
        assertNotNull(doctorRepository);

        doctor = TestUtils.createDoctor();
    }

    @Test
    public void createDoctorTest() {
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        Doctor newDoctor = doctorService.createDoctor(doctor);

        assertEquals(1L, newDoctor.getId());
        assertEquals("Wynne", newDoctor.getFirstName());
    }

    @Test
    public void updateDoctorInfoTest() {
        Map<String, Object> updatedInfo = new HashMap<>();
        updatedInfo.put("email", "archmage@aol.com");
        updatedInfo.put("password", "aneirin");

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        doctorService.updateDoctorInfo(doctor.getId(), updatedInfo);

        assertEquals("archmage@aol.com", doctor.getEmail());
        assertEquals("aneirin", doctor.getPassword());
    }

    @Test
    public void deleteDoctorTest() {
        assertNotNull(doctor);

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));
        doctorService.deleteDoctor(doctor.getId());

        verify(doctorRepository, times(1)).deleteById(doctor.getId());
    }

    @Test
    public void deleteDoctorExceptionTest() {
        assertThrows(DoctorNotFoundException.class, ()-> doctorService.deleteDoctor(1L));
    }
}
