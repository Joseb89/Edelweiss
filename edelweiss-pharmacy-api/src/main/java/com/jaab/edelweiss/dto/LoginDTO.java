package com.jaab.edelweiss.dto;

import com.jaab.edelweiss.model.Pharmacist;
import com.jaab.edelweiss.model.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

public record LoginDTO(Long id, String email, String password, Role role) implements UserDetails {

    public LoginDTO(Pharmacist pharmacist) {
        this(pharmacist.getId(), pharmacist.getEmail(), pharmacist.getPassword(), Role.PHARMACIST);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Set.of(new SimpleGrantedAuthority("ROLE_" + Role.PHARMACIST.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
