package com.example.emag.model.dao;

import com.example.emag.model.pojo.Address;

import java.sql.SQLException;
import java.util.List;

public interface IAddressDAO {

    void addAddress(long userId, Address address) throws SQLException;
    List<Address> getAllAddresses(long userId) throws SQLException;
    void updateAddress(Address address, long addressId) throws SQLException;
    Address getAddress(long addressId) throws SQLException;
    void deleteAddress(long addressId) throws SQLException;
}
