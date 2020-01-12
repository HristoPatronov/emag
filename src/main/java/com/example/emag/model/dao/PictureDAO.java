package com.example.emag.model.dao;

import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class PictureDAO {

    public void addPicture(Long productId, String url) throws SQLException {
        long pictureId;
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "INSERT INTO files (url, product_id) VALUES (?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, url);
            statement.setLong(2, productId);
            statement.executeUpdate();
        }
    }

    public String getPictureUrl(Long productId) throws SQLException {
        String pictureUrl;
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "SELECT url FROM files WHERE product_id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            ResultSet set = statement.executeQuery();
            set.next();
            pictureUrl = set.getString(1);
        }
        return pictureUrl;
    }
}
