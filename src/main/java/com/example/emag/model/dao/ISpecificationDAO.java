package com.example.emag.model.dao;

import com.example.emag.model.dto.SpecificationWithProductIdDTO;
import com.example.emag.model.pojo.Specification;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public interface ISpecificationDAO {

    void addSpecification(List<Specification> specification) throws SQLException;
    List<Specification> getSpecificationsForProduct(Long productID) throws SQLException;
    public void removeSpecification(Long specificationId) throws SQLException;
}
