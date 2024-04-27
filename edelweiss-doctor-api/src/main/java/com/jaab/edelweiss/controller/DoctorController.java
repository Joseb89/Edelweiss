package com.jaab.edelweiss.controller;

import com.jaab.edelweiss.config.AuthDTO;
import com.jaab.edelweiss.dto.LoginDTO;
import com.jaab.edelweiss.model.Doctor;
import com.jaab.edelweiss.service.DoctorService;
import com.jaab.edelweiss.service.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This class is a controller for the endpoints that create and maintain physician data
 *
 * @author Joseph Barr
 */
@RestController
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public String login(@RequestBody AuthDTO authDTO, HttpServletResponse response) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authDTO.email(), authDTO.password()));

        LoginDTO loginDTO = (LoginDTO) authentication.getPrincipal();

        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateToken(loginDTO);

            ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .secure(false)
                    .path("/physician/")
                    .build();

           response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

           return accessToken;

        } else
            throw new BadCredentialsException("Invalid email or password");
    }

    /**
     * Saves a new doctor to the doctor database
     *
     * @param doctor - the Doctor payload
     * @return - the new doctor
     */
    @PostMapping(value = "/newPhysician", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Doctor createDoctor(@RequestBody Doctor doctor) {
        return doctorService.createDoctor(doctor);
    }

    /**
     * Updates the doctor's information and merges it to the doctor database
     *
     * @param fields - the payload containing the updated information
     * @return - HTTP status response with the updated doctor
     */
    @PatchMapping(value = "/physician/updatePhysicianInfo",
            consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Doctor> updateDoctorInfo(@RequestBody Map<String, Object> fields) {
        return ResponseEntity.ok(doctorService.updateDoctorInfo(fields));
    }

    /**
     * Deletes a doctor from the doctor database based on their ID
     *
     * @param physicianId - the ID of the doctor
     * @return - HTTP status response
     */
    @DeleteMapping(value = "/physician/deleteDoctor/{physicianId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long physicianId) {
        doctorService.deleteDoctor(physicianId);

        return ResponseEntity.ok("Doctor successfully deleted.");
    }
}
