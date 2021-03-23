package persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DbConnection {
    private static Connection connection;

    public static Connection getConnection() {
        if (Objects.nonNull(connection))
            return connection;
        return createConnection();
    }

    private static Connection createConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:alley.db");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        connection = conn;
        return conn;
    }
}
