package com.jaab.edelweiss.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientDTO {

    private String firstName;
    private String lastName;
    private Long phoneNumber;
    private String streetAddress;
    private String city;
    private String state;
    private Integer zipcode;
    private String primaryDoctor;
    private String bloodType;
}
