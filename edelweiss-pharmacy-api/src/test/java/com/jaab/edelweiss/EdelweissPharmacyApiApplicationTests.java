package com.jaab.edelweiss;

import com.jaab.edelweiss.controller.PharmacistController;
import com.jaab.edelweiss.service.PharmacistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class EdelweissPharmacyApiApplicationTests {

    @Autowired
    private PharmacistController pharmacistController;

    @Autowired
    private PharmacistService pharmacistService;

    @Test
    void contextLoads() {
        assertNotNull(pharmacistController);
        assertNotNull(pharmacistService);
    }
}
