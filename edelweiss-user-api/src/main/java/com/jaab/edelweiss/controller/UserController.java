package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.dto.UserDTO;
import com.jaab.edelweiss.model.Role;
import com.jaab.edelweiss.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Saves a user with Patient role to the user database and sends ID to the patient API
     * @param userDTO - the UserDTO object from the patient API
     * @return = the ID of the new patient
     */
    @PostMapping(value = "/newPatient", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Long createPatient(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO, Role.PATIENT);
    }

    /**
     * Saves a user with Physician role to the user database and sends ID to the doctor API
     * @param userDTO - the UserDTO object from the doctor API
     * @return - the ID of the new doctor
     */
    @PostMapping(value = "/newPhysician", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Long createPhysician(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO, Role.PHYSICIAN);
    }

    /**
     * Saves a user with Pharmacist role to the user database and sends ID to the pharmacy API
     * @param userDTO - the UserDTO object from the pharmacy API
     * @return - the ID of the new pharmacist
     */
    @PostMapping(value = "/newPharmacist", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public Long createPharmacist(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO, Role.PHARMACIST);
    }

    /**
     * Updates the user info based on UserDTO payload from external API and merges it to the user database
     * @param userDTO - the UserDTO payload from the external API
     * @return - the UserDTO object containing the updated information
     */
    @PatchMapping(value = "/updateUserInfo", consumes = MediaType.APPLICATION_JSON_VALUE,
                    produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO updateUserInfo(@RequestBody UserDTO userDTO) {
        return userService.updateUserInfo(userDTO);
    }
}
