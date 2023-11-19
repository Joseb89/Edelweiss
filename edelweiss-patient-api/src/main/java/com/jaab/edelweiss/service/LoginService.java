package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dto.LoginDTO;
import com.jaab.edelweiss.model.Patient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

    private final PatientService patientService;

    public LoginService(PatientService patientService) {
        this.patientService = patientService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Patient> patient = patientService.getPatientByEmail(username);

        if (patient.isEmpty())
            throw new UsernameNotFoundException("Patient with specified email is not found.");

        return new LoginDTO(patient.get());
    }
}
