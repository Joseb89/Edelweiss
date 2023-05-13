package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Role;
import com.jaab.edelweiss.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(value = "/newPatient", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Long createPatient(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO, Role.PATIENT);
    }

    @PostMapping(value = "/newPhysician", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Long createPhysician(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO, Role.PHYSICIAN);
    }

    @PostMapping(value = "/newPharmacist", consumes = MediaType.APPLICATION_JSON_VALUE,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Long createPharmacist(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO, Role.PHARMACIST);
    }
}
