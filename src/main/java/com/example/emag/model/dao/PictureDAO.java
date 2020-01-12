package com.example.emag.model.dao;

import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class PictureDAO {

    public void addPicture(Long productId, String url) throws SQLException {
        long pictureId;
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "INSERT INTO files (url) VALUES (?);";
        try(PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, url);
            statement.executeUpdate();
            ResultSet keys = statement.getGeneratedKeys();
            keys.next();
            pictureId = keys.getLong(1);
        }
        sql = "INSERT INTO products_have_files (product_id, file_id) VALUES (?, ?);";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            statement.setLong(2, pictureId);
            statement.executeUpdate();
        }
    }

    public String getPictureUrl(Long productId) throws SQLException {
        String pictureUrl;
        Connection connection = DBManager.getInstance().getConnection();
        String sql = "SELECT f.url FROM files AS f JOIN products_have_files AS phf ON f.id = phf.file_id WHERE phf.product_id = ?;";
        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, productId);
            ResultSet set = statement.executeQuery();
            set.next();
            pictureUrl = set.getString(1);
        }
        return pictureUrl;
    }
}
