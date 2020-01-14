package com.example.emag.model.dao;

import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class PictureDAO {

    public void addPicture(Long productId, String url) throws SQLException {
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "INSERT INTO files (url, product_id) VALUES (?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, url);
            statement.setLong(2, productId);
            statement.executeUpdate();
        }
    }

    public List<String> getPicturesUrls(Long productId) throws SQLException {
        List<String> pictureUrls = new ArrayList<>();
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "SELECT url FROM files WHERE product_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            ResultSet set = statement.executeQuery();
            while (set.next()) {
                pictureUrls.add(set.getString(1));
            }
        }
        return pictureUrls;
    }

    public String getPictureUrl(Long productId) throws SQLException {
        String pictureUrl;
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "SELECT url FROM files WHERE product_id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            ResultSet set = statement.executeQuery();
            if (!set.next()) {
                return null;
            }
            pictureUrl = set.getString(1);
        }
        return pictureUrl;
    }
}
