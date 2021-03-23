package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDb {
    public static List<String> getUser(String id, String password) throws SQLException {
        String query = "select userType from user where id=? and password=?";
        PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, password);
        ResultSet resultSet = preparedStatement.executeQuery();
        List<String> availableTypes = new ArrayList<>();

        while (resultSet.next()) {
            availableTypes.add(resultSet.getString("userType"));
        }
        return availableTypes;
    }

    public static boolean isRegistered(String id) throws SQLException {
        String query = "select userType from user where id=?";
        PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public static void putUser(String id, String password, String type) throws SQLException {
        String query = "insert into user(id, password, userType) values (?,?,?)";
        PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, password);
        preparedStatement.setString(3, type);
        preparedStatement.execute();
    }
}
