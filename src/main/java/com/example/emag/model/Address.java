package com.example.emag.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Address {

    private Integer id;
    private String city;
    private String district;
    private String street;
    private String zip;
    private String phoneNumber;
    private Integer userId;

    public Address(String city, String district, String street, String zip, String phoneNumber, Integer userId) {
        this.city = city;
        this.district = district;
        this.street = street;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }

    public Address(Integer id, String city, String district, String street, String zip, String phoneNumber, Integer userId) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.street = street;
        this.zip = zip;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }
}
