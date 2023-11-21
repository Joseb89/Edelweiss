package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PatientRepository;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.exception.PatientNotFoundException;
import com.jaab.edelweiss.model.Patient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @InjectMocks
    private PatientService patientService;

    @Mock
    private PatientRepository patientRepository;

    @Test
    public void createPatientTest() {
        james.setAddress(jamesAddress);

        PatientDTO patientDTO = patientService.createPatient(james);

        assertEquals("Varric Tethras", patientDTO.primaryDoctor());
        assertEquals("O+", patientDTO.bloodType());
    }

    @Test
    public void getPatientByIdTest() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(james));

        PatientDTO patientDTO = patientService.getPatientById(james.getId());

        assertEquals("James", patientDTO.firstName());
        assertEquals("championofkirkwall@gmail.com", patientDTO.email());
    }

    @Test
    public void getPatientByIdExceptionTest() {
        assertThrows(PatientNotFoundException.class, () -> patientService.getPatientById(james.getId()));
    }

    @Test
    public void getPatientsByFirstNameTest() {
        when(patientRepository.getPatientsByFirstName(anyString())).thenReturn(getPatientsByFirstName());

        List<PatientDTO> patients = patientService.getPatientsByFirstName(firstNameTestParameter);

        assertEquals(1, patients.size());
    }

    @Test
    public void getPatientsByFirstNameExceptionTest() {
        assertThrows(PatientNotFoundException.class,
                () -> patientService.getPatientsByFirstName(firstNameTestParameter));
    }

    @Test
    public void getPatientsByLastNameTest() {
        when(patientRepository.getPatientsByLastName(anyString())).thenReturn(getPatientsByLastName());

        List<PatientDTO> patients = patientService.getPatientsByLastName(lastNameTestParameter);

        assertEquals(3, patients.size());
    }

    @Test
    public void getPatientsByLastNameExceptionTest() {
        assertThrows(PatientNotFoundException.class,
                () -> patientService.getPatientsByLastName(lastNameTestParameter));
    }

    @Test
    public void getPatientsByBloodTypeTest() {
        when(patientRepository.getPatientsByBloodType(anyString())).thenReturn(getPatientsByBloodType());

        List<PatientDTO> patients = patientService.getPatientsByBloodType(bloodTypeTestParameter);

        assertEquals(2, patients.size());
    }

    @Test
    public void getPatientsByBloodTypeExceptionTest() {
        assertThrows(PatientNotFoundException.class,
                () -> patientService.getPatientsByBloodType(bloodTypeTestParameter));
    }

    @Test
    public void getAddressTest() {
        bethany.setAddress(bethanyAddress);

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(bethany));

        AddressDTO addressDTO = patientService.getAddress(bethany.getId());

        assertEquals("59 Gallows St", addressDTO.streetAddress());
        assertEquals("San Antonio", addressDTO.city());
    }

    @Test
    public void getAddressExceptionTest() {
        assertThrows(PatientNotFoundException.class, () -> patientService.getAddress(bethany.getId()));
    }

    @Test
    public void updateAddressTest() {
        Patient patient = carver;
        patient.setAddress(carverAddress);

        assertEquals("San Antonio", patient.getAddress().getCity());
        assertEquals("TX", patient.getAddress().getState());

        Map<String, Object> updatedAddress = new HashMap<>();
        updatedAddress.put("StreetAddress", "515 Weisshaupt Ct");
        updatedAddress.put("city", "Boise");
        updatedAddress.put("state", "ID");
        updatedAddress.put("zipcode", 33247);

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));

        patientService.updateAddress(updatedAddress);

        assertEquals("Boise", patient.getAddress().getCity());
        assertEquals("ID", patient.getAddress().getState());
    }

    @Test
    public void updatePatientInfoTest() {
        assertEquals("circlemage@gmail.com", bethany.getEmail());
        assertEquals("daughterofamell", bethany.getPassword());

        Map<String, Object> updatedPatient = new HashMap<>();
        updatedPatient.put("email", "sisterofthechampion@yahoo.com");
        updatedPatient.put("password", "malcomsheir");

        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(bethany));

        patientService.updatePatientInfo(updatedPatient);

        assertEquals("sisterofthechampion@yahoo.com", bethany.getEmail());
        assertEquals("malcomsheir", bethany.getPassword());
    }

    @Test
    public void deletePatientTest() {
        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(james));

        patientService.deletePatient(james.getId());

        verify(patientRepository, times(1)).deleteById(james.getId());
    }

    @Test
    public void deletePatientExceptionTest() {
        assertThrows(PatientNotFoundException.class, () -> patientService.deletePatient(james.getId()));
    }
}
