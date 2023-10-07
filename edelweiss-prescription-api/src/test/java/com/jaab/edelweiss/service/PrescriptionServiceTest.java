package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PrescriptionRepository;
import com.jaab.edelweiss.dto.PrescriptionDTO;
import com.jaab.edelweiss.dto.PrescriptionStatusDTO;
import com.jaab.edelweiss.dto.UpdatePrescriptionDTO;
import com.jaab.edelweiss.exception.PrescriptionNotFoundException;
import com.jaab.edelweiss.model.Prescription;
import com.jaab.edelweiss.model.Status;
import com.jaab.edelweiss.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

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

    private Prescription potion, phoenixDown, darkMatter;

    @BeforeEach
    public void init() {
        potion = TestUtils.potion;
        phoenixDown = TestUtils.phoenixDown;
        darkMatter = TestUtils.darkMatter;
    }

    @Test
    public void createPrescriptionTest() {
        PrescriptionDTO prescriptionDTO = prescriptionService.createPrescription(TestUtils.prescriptionDTO);

        assertEquals(prescriptionDTO.doctorFirstName(), "Rinoa");
        assertEquals(prescriptionDTO.doctorLastName(), "Heartily");
    }

    @Test
    public void getPrescriptionsByDoctorNameTest() {
        when(prescriptionRepository.getPrescriptionsByDoctorName(anyString(), anyString()))
                .thenReturn(TestUtils.getPrescriptionsByDoctorName());

        List<PrescriptionDTO> prescriptions =
                prescriptionService.getPrescriptionsByDoctorName(TestUtils.doctorFirstName, TestUtils.doctorLastName);

        assertEquals(prescriptions.size(), 2);
    }

    @Test
    public void getPrescriptionsByPrescriptionStatusTest() {
        when(prescriptionRepository.getPrescriptionsByPrescriptionStatus(any(Status.class)))
                .thenReturn(TestUtils.getPrescriptionsByPrescriptionStatus());

        List<PrescriptionDTO> prescriptions =
                prescriptionService.getPrescriptionsByPrescriptionStatus(Status.APPROVED);

        assertEquals(prescriptions.size(), 1);
    }

    @Test
    public void updatePrescriptionInfoTest() {
        assertEquals("Potion", potion.getPrescriptionName());
        assertEquals((byte) 20, potion.getPrescriptionDosage());

        UpdatePrescriptionDTO prescriptionDTO = TestUtils.updatePrescriptionDTO;

        when(prescriptionRepository.findById(anyLong())).thenReturn(Optional.of(potion));

        prescriptionService.updatePrescriptionInfo(prescriptionDTO, potion.getId());

        assertEquals("Hi-Potion", potion.getPrescriptionName());
        assertEquals((byte) 10, potion.getPrescriptionDosage());
    }

    @Test
    public void updatePrescriptionInfoExceptionTest() {
        UpdatePrescriptionDTO prescriptionDTO = TestUtils.updatePrescriptionDTO;

        assertThrows(PrescriptionNotFoundException.class, () ->
                prescriptionService.updatePrescriptionInfo(prescriptionDTO, potion.getId()));
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
