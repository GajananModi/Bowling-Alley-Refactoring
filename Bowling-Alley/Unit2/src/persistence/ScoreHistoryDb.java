package persistence; /**
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */


import Model.Score;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class ScoreHistoryDb {

	public static void addScore(String nick, String date, String score)
			throws SQLException {

		String query = "insert into score(nick, date, score) values(?,?,?)";
		Connection connection = DbConnection.getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, nick);
		preparedStatement.setString(2, date);
		preparedStatement.setString(3, score);

		preparedStatement.execute();
	}

	public static Vector getScores(String nick)
			throws SQLException {
		Connection connection = DbConnection.getConnection();
		String query = "select nick, date, score from score where nick=?";
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		preparedStatement.setString(1, nick);

		ResultSet resultSet = preparedStatement.executeQuery();
		Vector<Score> vector = new Vector<>();

		while (resultSet.next()) {
			Score score = new Score(resultSet.getString("nick"),
					resultSet.getString("date"),
					resultSet.getString("score"));
			vector.add(score);
		}
		return vector;
	}
}
