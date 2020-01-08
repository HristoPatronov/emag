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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(city, address.city) &&
                Objects.equals(district, address.district) &&
                Objects.equals(street, address.street) &&
                Objects.equals(zip, address.zip) &&
                Objects.equals(phoneNumber, address.phoneNumber) &&
                Objects.equals(user, address.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, district, street, zip, phoneNumber, user);
    }
}


