package com.example.emag.model.dto;

import com.example.emag.model.pojo.Address;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

    private long id;
    private String city;
    private String district;
    private String street;
    private String zip;
    private String phoneNumber;

    public AddressDTO(Address address) {
        setId(address.getId());
        setCity(address.getCity());
        setDistrict(address.getDistrict());
        setStreet(address.getStreet());
        setZip(address.getZip());
        setPhoneNumber(address.getPhoneNumber());
    }
}
