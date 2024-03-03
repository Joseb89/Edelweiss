package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dao.PharmacistRepository;
import com.jaab.edelweiss.dto.LoginDTO;
import com.jaab.edelweiss.model.Pharmacist;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

    private final PharmacistRepository pharmacistRepository;

    public LoginService(PharmacistRepository pharmacistRepository) {
        this.pharmacistRepository = pharmacistRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Pharmacist> pharmacist = pharmacistRepository.findByEmail(username);

        return pharmacist.map(LoginDTO::new)
                .orElseThrow(()-> new UsernameNotFoundException("Pharmacist with specified email is not found."));
    }
}
