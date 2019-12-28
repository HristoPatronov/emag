package com.example.emag.model;

public class Address {

    private Integer id;
    private String city;
    private String district;
    private String street;
    private String ZIP;
    private String phoneNumber;
    private Integer userId;

    public Address(Integer id, String city, String district, String street, String ZIP, String phoneNumber, Integer userId) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.street = street;
        this.ZIP = ZIP;
        this.phoneNumber = phoneNumber;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZIP() {
        return ZIP;
    }

    public void setZIP(String ZIP) {
        this.ZIP = ZIP;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
