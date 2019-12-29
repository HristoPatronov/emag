package com.example.emag.dao;

import com.example.emag.model.Address;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AddressDAO implements IAddressDAO {


    private static AddressDAO mInstance;

    private AddressDAO() {
    }

    public static AddressDAO getInstance() {
        if (mInstance == null) {
            mInstance = new AddressDAO();
        }
        return mInstance;
    }

    @Override
    public void addAddress(Integer userId, Address address) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO addresses (city, district, street, zip, phone_number, user_id) VALUES (?,?,?,?,?,?);";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, address.getCity());
            statement.setString(2, address.getDistrict());
            statement.setString(3, address.getStreet());
            statement.setString(4, address.getZip());
            statement.setString(5, address.getPhoneNumber());
            statement.setInt(6, userId);
            statement.executeUpdate();
        }
    }

    @Override
    public List<Address> getAllAddresses(Integer userId) throws SQLException {
        List<Address> addresses = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM addresses WHERE user_id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setLong(1, userId);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Address address = new Address(set.getInt(1),
                        set.getString(2),
                        set.getString(3),
                        set.getString(4),
                        set.getString(5),
                        set.getString(6),
                        set.getInt(7));
                addresses.add(address);
            }
        }
        return addresses;
    }

    @Override
    public void updateAddress(Address address, Integer addressId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "UPDATE addresses SET city = ? , district = ?, street = ?, zip = ?, phone_number = ? WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setString(1, address.getCity());
            statement.setString(2, address.getDistrict());
            statement.setString(3, address.getStreet());
            statement.setString(4, address.getZip());
            statement.setString(5, address.getPhoneNumber());
            statement.setInt(6, addressId);
            statement.executeUpdate();
        }
    }

    @Override
    //not necessary???
    public Address getAddress(Integer addressId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM addresses WHERE id = ?;";
        Address address = null;
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, addressId);
            ResultSet set = statement.executeQuery();
            set.next();
            address = new Address(set.getInt(1),
                    set.getString(2),
                    set.getString(3),
                    set.getString(4),
                    set.getString(5),
                    set.getString(6),
                    set.getInt(7));
        }
        return address;
    }

    @Override
    public void deleteAddress(Integer addressId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "DELETE FROM addresses WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, addressId);
            statement.executeUpdate();
        }
    }
}
