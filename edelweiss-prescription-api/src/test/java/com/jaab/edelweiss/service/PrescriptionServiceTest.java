package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PrescriptionRepository;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.exception.PrescriptionNotFoundException;
import com.jaab.edelweiss.model.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PrescriptionServiceTest {

    @InjectMocks
    private PrescriptionService prescriptionService;

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Test
    public void createPrescriptionTest() {
        PrescriptionDTO newPrescription = prescriptionService.createPrescription(prescriptionDTO);

        assertEquals("Rinoa", newPrescription.doctorFirstName());
        assertEquals("Heartily", newPrescription.doctorLastName());
    }

    @Test
    public void getPrescriptionsByDoctorNameTest() {
        when(prescriptionRepository.findByDoctorFirstNameAndDoctorLastName(anyString(), anyString()))
                .thenReturn(getPrescriptionsByDoctorName());

        List<PrescriptionDTO> prescriptions =
                prescriptionService.getPrescriptionsByDoctorName(doctorFirstName, doctorLastName);

        assertEquals(2, prescriptions.size());
    }

    @Test
    public void getPrescriptionsByPrescriptionStatusTest() {
        when(prescriptionRepository.findByPrescriptionStatus(any(Status.class)))
                .thenReturn(getPrescriptionsByPrescriptionStatus());

        List<PrescriptionDTO> prescriptions =
                prescriptionService.getPrescriptionsByPrescriptionStatus(Status.APPROVED);

        assertEquals(1, prescriptions.size());
    }

    @Test
    public void updatePrescriptionInfoTest() {
        assertEquals("Potion", potion.getPrescriptionName());
        assertEquals((byte) 20, potion.getPrescriptionDosage());

        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(potion));

        prescriptionService.updatePrescriptionInfo(updatePrescriptionDTO, potion.getId());

        assertEquals("Hi-Potion", potion.getPrescriptionName());
        assertEquals((byte) 10, potion.getPrescriptionDosage());
    }

    @Test
    public void updatePrescriptionInfoExceptionTest() {
        assertThrows(PrescriptionNotFoundException.class, () ->
                prescriptionService.updatePrescriptionInfo(updatePrescriptionDTO, potion.getId()));
    }

    @Test
    public void approvePrescriptionTest() {
        assertEquals(Status.PENDING, phoenixDown.getPrescriptionStatus());

        PrescriptionStatusDTO status = new PrescriptionStatusDTO(Status.DENIED);

        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(phoenixDown));

        prescriptionService.approvePrescription(status, phoenixDown.getId());

        assertEquals(Status.DENIED, phoenixDown.getPrescriptionStatus());
    }

    @Test
    public void approvePrescriptionExceptionTest() {
        PrescriptionStatusDTO status = new PrescriptionStatusDTO(Status.DENIED);

        assertThrows(PrescriptionNotFoundException.class, () ->
                prescriptionService.approvePrescription(status, phoenixDown.getId()));
    }

    @Test
    public void deletePrescriptionTest() {
        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(darkMatter));

        prescriptionService.deletePrescription(darkMatter.getId());

        verify(prescriptionRepository, times(1)).deleteById(darkMatter.getId());
    }

    @Test
    public void deletePrescriptionExceptionTest() {
        assertThrows(PrescriptionNotFoundException.class, () ->
                prescriptionService.deletePrescription(darkMatter.getId()));
    }
}
