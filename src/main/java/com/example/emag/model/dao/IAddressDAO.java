package com.example.emag.model.dao;

import com.example.emag.model.pojo.Address;

import java.sql.SQLException;
import java.util.List;

public interface IAddressDAO {

    void addAddress(Integer userId, Address address) throws SQLException;
    List<Address> getAllAddresses(Integer userId) throws SQLException;
    void updateAddress(Address address, Integer addressId) throws SQLException;
    Address getAddress(Integer addressId) throws SQLException;   //not necessary???
    void deleteAddress(Integer addressId) throws SQLException;
}
