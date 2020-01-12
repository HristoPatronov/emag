package com.example.emag.controller;

import com.example.emag.model.dao.SpecificationDAO;
import com.example.emag.model.pojo.Specification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
public class SpecificationController {

    @Autowired
    private SpecificationDAO specificationDAO;

    @GetMapping("/specifications/{productId}")
    public List<Specification> getSpecification(@PathVariable(name = "productId") long productId) throws SQLException {
        return specificationDAO.getSpecificationsForProduct(productId);
    }
}
