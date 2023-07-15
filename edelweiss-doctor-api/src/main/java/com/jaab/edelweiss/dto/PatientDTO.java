package com.jaab.edelweiss.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTO {

    private  Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Long phoneNumber;
    private String primaryDoctor;
    private String bloodType;
}
