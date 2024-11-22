package sqlOperations;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class SqlMethods extends BaseClass {

	ResultSet resultSet = null;
	DatabaseMetaData metaData = null;

	public <T> List<T> getMetaData(String userName, String password, String url, String querie, String what)
			throws SQLException {

		List<T> dataList = new ArrayList<>();
		List<T> typeList = new ArrayList<>();
		Connection conn = (Connection) connectToDatabase(url, userName, password);
		Statement st = conn.createStatement();
		resultSet = st.executeQuery(querie);
		ResultSetMetaData data = resultSet.getMetaData();
		int collCount = data.getColumnCount();
		for (int a = 1; a <= collCount; a++) {
			dataList.add((T) data.getColumnName(a));
			typeList.add((T) data.getColumnTypeName(a));
		}
		if (!what.contains("name")) {
			return typeList;
		}
		return dataList;
	}

	public Object executeQuerie(String userName, String password, String url, String query) throws SQLException {

		String message = "";
		try {
			Connection conn = (Connection) connectToDatabase(url, userName, password);
			Statement st = conn.createStatement();
			resultSet = st.executeQuery(query);
			return resultSet;
		} catch (SQLException e) {
			message = e.getMessage();
		}
		return message;

	};

	public <T> T executeQuerie2(String userName, String password, String url, String query) throws SQLException {

		String message = "";
		try {
			Connection conn = (Connection) connectToDatabase(url, userName, password);
			Statement st = conn.createStatement();
			resultSet = st.executeQuery(query);
			return (T) resultSet;
		} catch (SQLException e) {
			message = e.getMessage();
		}
		return (T) message;

	};

	public Object addIntoDatabase(String userName, String password, String url, String query, List<List<String>> data)
			throws SQLException {

		Connection conn = (Connection) connectToDatabase(url, userName, password);
		PreparedStatement prSt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
		int[] rowsAffected;
		String message = "";

		try {
			for (int a = 0; a < data.size(); a++) {
				prSt.setString(1, data.get(a).get(0));
				prSt.setString(2, data.get(a).get(1));
				prSt.setString(3, data.get(a).get(2));
				prSt.setString(4, data.get(a).get(3));
				prSt.addBatch();
			}
			rowsAffected = prSt.executeBatch();
		} catch (SQLException e) {
			message = e.getMessage();
			return message;
		}

		return rowsAffected;
	}

	public <T> T updateDatabase(String userName, String password, String url, String query, String newname, int id)
			throws SQLException {

		String message = "";
		Integer number = 0;
		try {
			Connection conn = (Connection) connectToDatabase(url, userName, password);
			PreparedStatement prSt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			prSt.setString(1, newname);
			prSt.setInt(2, id);
			number = prSt.executeUpdate();
			return (T) number;
		} catch (SQLException e) {
			message = e.getMessage();
		}
		return (T) message;
	}

}
