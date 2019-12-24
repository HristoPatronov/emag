package dao;

import java.sql.SQLException;
import java.util.HashMap;

public interface ISpecificationDAO {

    HashMap<String, HashMap<String, String>> getSpecificationsForProduct(Integer productID) throws SQLException;
}
