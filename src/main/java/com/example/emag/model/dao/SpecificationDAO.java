package com.example.emag.model.dao;

import com.example.emag.model.dto.SpecificationWithProductIdDTO;
import com.example.emag.model.pojo.Specification;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Component
public class SpecificationDAO implements ISpecificationDAO {

    @Override
    public void addSpecification(List<Specification> specifications) throws SQLException {
        List<Specification.Spec> specs = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO specifications (title, desc_title, description, product_id ) VALUES (?, ?, ?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            for (Specification specification : specifications){
                specs = specification.getSpecifications();
                for (Specification.Spec spec : specs){
                    statement.setString(1, specification.getTitle());
                    statement.setString(2, spec.getDescTitle());
                    statement.setString(3, spec.getDescription());
                    statement.setLong(4, specification.getProductId());
                    statement.executeUpdate();
                }
            }
        }
    }

    @Override
    public List<Specification> getSpecificationsForProduct(Long productID) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM specifications WHERE product_id = ?;";
        List<Specification> specs = new ArrayList<>();
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setLong(1, productID);
            ResultSet set = statement.executeQuery();
            while (set.next()){
                Specification specification = new Specification();
                specification.setId(set.getLong(1));
                specification.setTitle(set.getString(2));
                specification.getSpecifications().add(new Specification.Spec(set.getString(3), set.getString(4)));
                specification.setProductId(set.getLong(5));
                specs.add(specification);
            }
        }
        return  specs;
    }

    @Override
    public void removeSpecification(Long specificationId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "DELETE FROM specifications WHERE id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setLong(1, specificationId);
            statement.executeUpdate();
        }
    }
}
