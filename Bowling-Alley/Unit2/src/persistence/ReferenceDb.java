package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReferenceDb {
    public static Object getEntry(String key) throws SQLException {
        String query = "select entry from reference where key=?";
        PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, key);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return resultSet.getString("entry");
        } else {
            return null;
        }
    }

    public void putEntry(String key, Object entry) throws SQLException {
        String query = "insert into reference(key, entry) values (?,?)";
        PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, key);
        preparedStatement.setString(2, String.valueOf(entry));
        preparedStatement.execute();
    }
}
