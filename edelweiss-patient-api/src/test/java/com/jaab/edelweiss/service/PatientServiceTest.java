package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PatientRepository;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.exception.PatientNotFoundException;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.utils.PatientUtils;
import com.jaab.edelweiss.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientUtils patientUtils;

    private Patient james, bethany, carver;

    @BeforeEach
    public void init() {
        james = TestUtils.james;
        james.setAddress(TestUtils.jamesAddress);

        bethany = TestUtils.bethany;
        bethany.setAddress(TestUtils.bethanyAddress);

        carver = TestUtils.carver;
        carver.setAddress(TestUtils.carverAddress);
    }

    @Test
    public void createPatientTest() {
        when(patientRepository.save(any(Patient.class))).thenReturn(james);
        PatientDTO patientDTO = patientService.createPatient(james);

        assertEquals(1L, patientDTO.id());
        assertEquals("Varric Tethras", patientDTO.primaryDoctor());
    }

    @Test
    public void getPatientByIdTest() {
        when(patientUtils.getPatientById(anyLong())).thenReturn(james);
        PatientDTO patientDTO = patientService.getPatientById(james.getId());

        assertEquals("James", patientDTO.firstName());
        assertEquals("championofkirkwall@gmail.com", patientDTO.email());
    }

    @Test
    public void getPatientByIdExceptionTest() {
        when(patientUtils.getPatientById(anyLong())).thenThrow(PatientNotFoundException.class);
        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientById(4L));
    }

    @Test
    public void getPatientsByFirstNameTest() {
        when(patientRepository.getPatientsByFirstName(anyString())).thenReturn(TestUtils.getPatientsByFirstName());
        List<PatientDTO> patients = patientService.getPatientsByFirstName(TestUtils.firstNameTestParameter);
        assertEquals(1, patients.size());
    }

    @Test
    public void getPatientsByFirstNameExceptionTest() {
        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientsByFirstName("Fenris"));
    }

    @Test
    public void getPatientsByLastNameTest() {
        when(patientRepository.getPatientsByLastName(anyString())).thenReturn(TestUtils.getPatientsByLastName());
        List<PatientDTO> patients = patientService.getPatientsByLastName(TestUtils.lastNameTestParameter);
        assertEquals(3, patients.size());
    }

    @Test
    public void getPatientsByLastNameExceptionTest() {
        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientsByLastName("Vallen"));
    }

    @Test
    public void getPatientsByBloodTypeTest() {
        when(patientRepository.getPatientsByBloodType(anyString())).thenReturn(TestUtils.getPatientsByBloodType());
        List<PatientDTO> patients = patientService.getPatientsByBloodType(TestUtils.bloodTypeTestParameter);
        assertEquals(2, patients.size());
    }

    @Test
    public void getPatientsByBloodTypeExceptionTest() {
        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientsByBloodType("B+"));
    }

    @Test
    public void getAddressTest() {
        when(patientUtils.getPatientById(anyLong())).thenReturn(bethany);
        AddressDTO addressDTO = patientService.getAddress(bethany.getId());

        assertEquals("59 Gallows St", addressDTO.streetAddress());
        assertEquals("San Antonio", addressDTO.city());
    }

    @Test
    public void updateAddressTest() {
        assertEquals("San Antonio", carver.getAddress().getCity());
        assertEquals("TX", carver.getAddress().getState());

        Map<String, Object> updatedAddress = new HashMap<>();
        updatedAddress.put("StreetAddress", "515 Weisshaupt Ct");
        updatedAddress.put("city", "Boise");
        updatedAddress.put("state", "ID");
        updatedAddress.put("zipcode", 33247);

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(carver));
        patientService.updateAddress(carver.getId(), updatedAddress);

        assertEquals("Boise", carver.getAddress().getCity());
        assertEquals("ID", carver.getAddress().getState());
    }

    @Test
    public void updatePatientInfoTest() {
        assertEquals("circlemage@gmail.com", bethany.getEmail());
        assertEquals("daughterofamell", bethany.getPassword());

        Map<String, Object> updatedPatient = new HashMap<>();
        updatedPatient.put("email", "sisterofthechampion@yahoo.com");
        updatedPatient.put("password", "malcomsheir");

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(bethany));
        patientService.updatePatientInfo(bethany.getId(), updatedPatient);

        assertEquals("sisterofthechampion@yahoo.com", bethany.getEmail());
        assertEquals("malcomsheir", bethany.getPassword());
    }

    @Test
    public void deletePatientTest() {
        assertNotNull(james);

        when(patientUtils.getPatientById(anyLong())).thenReturn(james);
        patientService.deletePatient(james.getId());

        verify(patientRepository, times(1)).deleteById(james.getId());
    }
}
