package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dto.LoginDTO;
import com.jaab.edelweiss.model.Doctor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

    private final DoctorService doctorService;

    public LoginService(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Doctor> doctor = doctorService.getDoctorByEmail(username);

        if (doctor.isEmpty())
            throw new UsernameNotFoundException("Physician with specified email is not found.");

        return new LoginDTO(doctor.get());
    }
}
