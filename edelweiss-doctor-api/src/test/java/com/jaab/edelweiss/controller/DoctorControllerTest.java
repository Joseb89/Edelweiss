package com.jaab.edelweiss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.service.DoctorService;
import com.jaab.edelweiss.utils.TestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = DoctorController.class)
public class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DoctorService doctorService;

    private Doctor doctor;

    @BeforeEach
    void init() {
        doctor = TestUtils.createDoctor(TestUtils.ID);
    }

    @Test
    public void createDoctorTest() throws Exception {
        when(doctorService.createDoctor(any(Doctor.class))).thenReturn(doctor);

        this.mockMvc.perform(post("/newPhysician")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(doctor)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void updateDoctorInfoTest() throws Exception {
        Doctor updatedInfo = new Doctor(doctor.getId(), doctor.getFirstName(), doctor.getLastName(),
                "archmage@aol.com", doctor.getPassword(), doctor.getPhoneNumber(), doctor.getPractice(),
                doctor.getRole());

        when(doctorService.updateDoctorInfo(anyLong(), anyMap())).thenReturn(updatedInfo);

        this.mockMvc.perform(patch("/physician/" + doctor.getId() + "/updatePhysicianInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedInfo)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("archmage@aol.com"));
    }

    @Test
    public void deleteDoctorTest() throws Exception {
        this.mockMvc.perform(delete("/physician/deleteDoctor/" + doctor.getId()))
                .andExpect(status().isOk());
    }
}
