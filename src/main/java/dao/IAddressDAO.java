package dao;

import model.Address;

import java.util.Collection;

public interface IAddressDAO {

    void addAddress(Long userId, Address address);

    Collection<Address> getAllAddresses(Long userId);

    void updateAddress(Long userId, Long addressId);

    Address getAddress(Long addressId);

    void deleteAddress(Long addressId);
}
