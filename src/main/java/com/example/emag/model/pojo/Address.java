package com.example.emag.model.pojo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

    private long id;
    private String city;
    private String district;
    private String street;
    private String zip;
    private String phoneNumber;
    private User user;

    @Override
    public int hashCode() {
        return Objects.hash(city, district, street, zip, phoneNumber, user);
    }
}


