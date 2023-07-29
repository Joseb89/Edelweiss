package com.jaab.edelweiss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    public void init() {
        assertThat(userService).isNotNull();
        userDTO = new UserDTO(1L, "Squall", "Leonheart", "finalfantasy8@gmail.com",
                "gunblade");
    }

    @Test
    public void createPatientTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/newPatient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void createPhysicianTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/newPhysician")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void createPharmacistTest() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/newPharmacist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDTO)))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUserInfoTest() throws Exception {
        UserDTO updatedData = new UserDTO(1L, null, null, null, "rinoa");

        mockMvc.perform(MockMvcRequestBuilders.patch("/updateUserInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedData)))
                .andExpect(status().isOk());

    }

    @Test
    public void deleteUserTest() throws Exception {
        mockMvc.perform((MockMvcRequestBuilders.delete("/deleteUser/{userId}", 1L)))
                .andExpect(status().isOk());
    }

}
