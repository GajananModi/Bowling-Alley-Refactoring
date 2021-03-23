package persistence;/* persistence.BowlerFile.java
 *
 *  Version:
 *  		$Id$
 *
 *  Revisions:
 * 		$Log: persistence.BowlerFile.java,v $
 * 		Revision 1.5  2003/02/02 17:36:45  ???
 * 		Updated comments to match javadoc format.
 *
 * 		Revision 1.4  2003/02/02 16:29:52  ???
 * 		Added events.ControlDeskEvent and observer.ControlDeskObserver. Updated entity.Queue to allow access to Vector so that contents could be viewed without destroying. Implemented observer model for most of entity.ControlDesk.
 *
 *
 */

/**
 * Class for interfacing with entity.Bowler database
 */


import Model.Bowler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class BowlerDb {

	/**
	 * Retrieves bowler information from the database and returns a entity.Bowler objects with populated fields.
	 *
	 * @param nickName    the nickName of the bolwer to retrieve
	 *
	 * @return a entity.Bowler object
	 *
	 */

	public static Bowler getBowlerInfo(String nickName)
			throws SQLException {

		Connection conn = DbConnection.getConnection();
		PreparedStatement statement = conn.prepareStatement("select * from bowler where nick=?");
		statement.setString(1, nickName);
		ResultSet resultSet = statement.executeQuery();
		while (resultSet.next()) {
			Bowler bowler = new Bowler(
					resultSet.getString("nick"),
					resultSet.getString("full"),
					resultSet.getString("email")
			);
			return bowler;
		}
		return null;
	}

	/**
	 * Stores a entity.Bowler in the database
	 *
	 * @param nickName    the NickName of the entity.Bowler
	 * @param fullName    the FullName of the entity.Bowler
	 * @param email    the E-mail Address of the entity.Bowler
	 *
	 */

	public static void putBowlerInfo(
			String nickName,
			String fullName,
			String email)
			throws SQLException {

		String query = "insert into bowler(nick,full,email) values(?,?,?)";
		Connection conn = DbConnection.getConnection();
		PreparedStatement preparedStatement = conn.prepareStatement(query);
		preparedStatement.setString(1, nickName);
		preparedStatement.setString(2, fullName);
		preparedStatement.setString(3, email);
		preparedStatement.execute();
	}

	/**
	 * Retrieves a list of nicknames in the bowler database
	 *
	 * @return a Vector of Strings
	 *
	 */

	public static Vector getBowlers()
			throws SQLException {

		Connection conn = DbConnection.getConnection();
		PreparedStatement statement = conn.prepareStatement("select nick from bowler");
		ResultSet resultSet = statement.executeQuery();
		Vector<String> vector = new Vector<>();
		while (resultSet.next()) {
			vector.add(resultSet.getString("nick"));
		}
		return vector;
	}

}