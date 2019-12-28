package com.example.emag.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class SpecificationDAO implements ISpecificationDAO {


    private static SpecificationDAO mInstance;

    private SpecificationDAO() {
    }

    public static SpecificationDAO getInstance() {
        if (mInstance == null) {
            mInstance = new SpecificationDAO();
        }
        return mInstance;
    }

    @Override
    public void addSpecificationInDb(Integer productId, HashMap<String, HashMap<String, String>> specifications) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "INSERT INTO specifications (title, desc_title, description, product_id) VALUES (?, ?, ?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            for (String title : specifications.keySet()){
                HashMap<String, String> spec = specifications.get(title);
                for (Map.Entry<String, String> entry : spec.entrySet()){
                    statement.setString(1, title);
                    statement.setString(2, entry.getKey());
                    statement.setString(3, entry.getValue());
                    statement.setInt(4, productId);
                    statement.executeUpdate();
                }
            }
        }
    }

    @Override
    public HashMap<String, HashMap<String, String>> getSpecificationsForProduct(Integer productID) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "SELECT * FROM specifications WHERE product_id = ?;";
        HashMap<String, HashMap<String, String>> specs = new HashMap<>();
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            ResultSet set = statement.executeQuery();
            while (set.next()){
                if (!specs.containsKey(set.getString("title"))){
                    specs.put(set.getString("title"), new HashMap<>());
                }
                specs.get(set.getString("title")).put(set.getString("desc_title"), set.getString("description"));
            }
        }
        return  specs;
    }

    @Override
    public void removeSpecification(Integer productId) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String url = "DELETE FROM specifications WHERE product_id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(url)) {
            statement.setInt(1, productId);
            statement.executeUpdate();
        }
    }
}
