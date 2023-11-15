package com.jaab.edelweiss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.AddressDTO;
import com.jaab.edelweiss.dto.PatientDTO;
import com.jaab.edelweiss.model.Patient;
import com.jaab.edelweiss.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.jaab.edelweiss.utils.TestUtils.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PatientController.class)
public class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    @Test
    public void createPatientTest() throws Exception {
        PatientDTO patientDTO = new PatientDTO(james);

        when(patientService.createPatient(any(Patient.class))).thenReturn(patientDTO);

        this.mockMvc.perform(post("/newPatient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("James"));
    }

    @Test
    public void updateAddressTest() throws Exception {
        AddressDTO updatedAddress = new AddressDTO("654 Adamant Ave", "Boise","ID", 96521);

        when(patientService.updateAddress(anyLong(), anyMap())).thenReturn(updatedAddress);

        this.mockMvc.perform(patch("/patient/" + carver.getId() + "/updateAddress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAddress))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Boise"));
    }

    @Test
    public void updatePatientInfoTest() throws Exception {
        PatientDTO updatedInfo = new PatientDTO(bethany.getId(), bethany.getFirstName(),"Amell",
                bethany.getEmail(), bethany.getPhoneNumber(), bethany.getPrimaryDoctor(), bethany.getBloodType());

        when(patientService.updatePatientInfo(anyLong(), anyMap())).thenReturn(updatedInfo);

        this.mockMvc.perform(patch("/patient/" + bethany.getId() + "/updatePatientInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((objectMapper.writeValueAsString(updatedInfo)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Amell"));
    }
}
