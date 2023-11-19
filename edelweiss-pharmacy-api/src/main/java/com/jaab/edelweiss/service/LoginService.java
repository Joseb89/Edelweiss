package com.jaab.edelweiss.service;

import com.jaab.edelweiss.dto.LoginDTO;
import com.jaab.edelweiss.model.Pharmacist;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

    private final PharmacistService pharmacistService;

    public LoginService(PharmacistService pharmacistService) {
        this.pharmacistService = pharmacistService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Pharmacist> pharmacist = pharmacistService.getPharmacistByEmail(username);

        if (pharmacist.isEmpty())
            throw new UsernameNotFoundException("Pharmacist with specified email is not found.");

        return new LoginDTO(pharmacist.get());
    }
}
