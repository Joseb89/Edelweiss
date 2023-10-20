package com.jaab.edelweiss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.model.Address;
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
        when(patientService.createPatient(any(Patient.class))).thenReturn(james);

        this.mockMvc.perform(post("/newPatient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(james)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("James"));
    }

    @Test
    public void updateAddressTest() throws Exception {
        Address updatedAddress = new Address(carver.getId(), carver, "654 Adamant Ave", "Boise",
                "ID", 96521);

        when(patientService.updateAddress(anyLong(), anyMap())).thenReturn(updatedAddress);

        this.mockMvc.perform(patch("/patient/" + carver.getId() + "/updateAddress")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedAddress)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.city").value("Boise"));
    }

    @Test
    public void updatePatientInfoTest() throws Exception {
        Patient updatedInfo = new Patient(bethany.getId(), bethany.getFirstName(), "Amell",
                bethany.getEmail(), bethany.getPassword(), bethany.getAddress(), bethany.getPhoneNumber(),
                bethany.getPrimaryDoctor(), bethany.getBloodType(), bethany.getRole());

        when(patientService.updatePatientInfo(anyLong(), anyMap())).thenReturn(updatedInfo);

        this.mockMvc.perform(patch("/patient/" + bethany.getId() + "/updatePatientInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName").value("Amell"));
    }
}
