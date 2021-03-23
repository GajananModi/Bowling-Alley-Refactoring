package persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SearchDb {
    public static Object[] getTables() throws SQLException {
        String query = "select tbl_name from sqlite_master";
        ResultSet resultSet = DbConnection.getConnection().createStatement().executeQuery(query);
        List<String> list = new ArrayList<>();
        while (resultSet.next()) {
            list.add(resultSet.getString("tbl_name"));
        }
        return list.toArray();
    }

    public static Object[] getColumnsByTable(String table) throws SQLException {
        String query = "select * from " + table;
        ResultSet resultSet = DbConnection.getConnection().createStatement().executeQuery(query);
        int colcount = resultSet.getMetaData().getColumnCount();
        Vector<String> vector = new Vector<>();
        for (int i = 1; i <= colcount; i++) {
            vector.add(resultSet.getMetaData().getColumnName(i));
        }
        return vector.toArray();
    }

    public static List<Map<String, String>> getQueryResult(String on, String using, String value) throws SQLException {
        String query = String.format("select * from %s where %s=?", on, using);
        PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(query);
        preparedStatement.setString(1, value);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getMapFromResultSet(resultSet);
    }

    public static List<Map<String, String>> getAllBowlers() throws SQLException {
        String query = String.format("select * from bowler");
        PreparedStatement preparedStatement = DbConnection.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        return getMapFromResultSet(resultSet);
    }

    public static List<Map<String, String>> getPlayerWiseScores(String type) throws SQLException {
        String query;
        switch (type) {
            case "AVG":
                query = "select nick, avg(score) from score group by nick order by cast(avg(score) as float) desc";
                break;
            case "MAX":
                query = "select nick, max(score) from score group by nick order by cast(max(score) as integer) desc";
                break;
            case "MIN":
                query = "select nick, min(score) from score group by nick order by cast(min(score) as integer)";
                break;
            default:
                return new ArrayList<>();
        }
        ResultSet resultSet = DbConnection.getConnection().createStatement().executeQuery(query);
        return getMapFromResultSet(resultSet);
    }

    private static List<Map<String, String>> getMapFromResultSet(ResultSet resultSet) throws SQLException {
        List<Map<String, String>> result = new ArrayList<>();
        while (resultSet.next()) {
            int i = 1;
            Map<String, String> map = new LinkedHashMap<>();
            while (i <= resultSet.getMetaData().getColumnCount()) {
                map.put(resultSet.getMetaData().getColumnName(i), resultSet.getString(i));
                i++;
            }
            result.add(map);
        }
        return result;
    }
}
