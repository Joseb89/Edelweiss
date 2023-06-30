package com.jaab.edelweiss.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

    public UserDTO(Long id) {
        this.id = id;
    }

    public UserDTO(Long id, String lastName) {
        this.id = id;
        this.lastName = lastName;
    }
}
