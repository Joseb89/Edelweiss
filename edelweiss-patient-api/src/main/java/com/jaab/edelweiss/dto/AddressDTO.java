package com.jaab.edelweiss.dto;

import com.jaab.edelweiss.model.Address;

public record AddressDTO(String streetAddress, String city, String state, Integer zipcode) {

    public AddressDTO(Address address) {
        this(address.getStreetAddress(), address.getCity(), address.getState(), address.getZipcode());
    }
}
