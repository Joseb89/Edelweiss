package com.jaab.edelweiss.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDTO {

    private  Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNumber;
    private String primaryDoctor;
    private String bloodType;
}
