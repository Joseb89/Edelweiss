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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DoctorServiceTest {

    @InjectMocks
    private DoctorService doctorService;

    @Mock
    private DoctorRepository doctorRepository;

    private Doctor doctor;

    @BeforeEach
    void init() {
        doctor = TestUtils.createDoctor(TestUtils.ID);
    }

    @Test
    public void createDoctorTest() {
        Doctor newDoctor = doctorService.createDoctor(doctor);

        assertEquals(1L, newDoctor.getId());
        assertEquals("Wynne", newDoctor.getFirstName());
    }

    @Test
    public void updateDoctorInfoTest() {
        assertEquals("seniorenchanter@aol.com", doctor.getEmail());
        assertEquals("spiritoffaith", doctor.getPassword());

        Map<String, Object> updatedInfo = new HashMap<>();
        updatedInfo.put("email", "archmage@aol.com");
        updatedInfo.put("password", "aneirin");

        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        doctorService.updateDoctorInfo(updatedInfo);

        assertEquals("archmage@aol.com", doctor.getEmail());
        assertEquals("aneirin", doctor.getPassword());
    }

    @Test
    public void deleteDoctorTest() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.of(doctor));

        doctorService.deleteDoctor(doctor.getId());

        verify(doctorRepository, times(1)).deleteById(doctor.getId());
    }

    @Test
    public void deleteDoctorExceptionTest() {
        assertThrows(DoctorNotFoundException.class, () -> doctorService.deleteDoctor(doctor.getId()));
    }
}
