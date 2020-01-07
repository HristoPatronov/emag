package com.example.emag.model.dao;

import java.sql.SQLException;
import java.util.HashMap;

public interface ISpecificationDAO {

    void addSpecificationInDb(Integer productId, HashMap<String, HashMap<String, String>> specifications) throws SQLException;
    HashMap<String, HashMap<String, String>> getSpecificationsForProduct(Integer productID) throws SQLException;

    public void removeSpecification(Integer productId) throws SQLException;
}
