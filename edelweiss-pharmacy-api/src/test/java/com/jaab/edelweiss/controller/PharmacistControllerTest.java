package com.jaab.edelweiss.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaab.edelweiss.model.Pharmacist;
import com.jaab.edelweiss.service.PharmacistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.jaab.edelweiss.utils.TestUtils.createPharmacist;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PharmacistController.class)
public class PharmacistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PharmacistService pharmacistService;

    private Pharmacist pharmacist;

    @BeforeEach
    void init() {
        pharmacist = createPharmacist();
    }

    @Test
    public void createPharmacistTest() throws Exception {
        when(pharmacistService.createPharmacist(any(Pharmacist.class))).thenReturn(pharmacist);

        this.mockMvc.perform(post("/newPharmacist")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pharmacist)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.lastName").value("Theirin"));
    }

    @Test
    public void updatePharmacistInfoTest() throws Exception {
        Pharmacist updatedPharmacist = new Pharmacist(pharmacist.getId(), pharmacist.getFirstName(),
                pharmacist.getLastName(), "greywarden@gmail.com", pharmacist.getPassword());

        when(pharmacistService.updatePharmacistInfo(anyLong(), anyMap())).thenReturn(updatedPharmacist);

        this.mockMvc.perform(patch("/pharmacy/" + pharmacist.getId() + "/updatePharmacistInfo")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPharmacist)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("greywarden@gmail.com"));
    }

    @Test
    public void deletePharmacistTest() throws Exception {
        this.mockMvc.perform(delete("/pharmacy/deletePharmacist/" + pharmacist.getId()))
                .andExpect(status().isOk());
    }
}
