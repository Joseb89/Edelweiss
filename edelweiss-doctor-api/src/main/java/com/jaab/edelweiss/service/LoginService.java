package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.DoctorRepository;
import com.jaab.edelweiss.dto.LoginDTO;
import com.jaab.edelweiss.model.Doctor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

    private final DoctorRepository doctorRepository;

    public LoginService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Doctor> doctor = doctorRepository.findByEmail(username);

        return doctor.map(LoginDTO::new)
                .orElseThrow(()-> new UsernameNotFoundException("Physician with specified email is not found."));
    }
}
