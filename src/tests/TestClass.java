package tests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.sql.Connection;
import org.testng.Assert;
import org.testng.annotations.Test;

import sqlOperations.MyUtil;
import sqlOperations.SqlMethods;

public class TestClass extends BaseTest {

	@Test(priority = 0)
	public void test00_succsesfull_connection() throws SQLException {

		Boolean status = false;
		Object object = SQLhandler.connectToDatabase(url, user, password);
		if (!(object instanceof String)) {
			Connection conn = (Connection) object;
			status = conn.isValid(5);

		}
		Assert.assertEquals(status, true);

	};

	@Test(priority = 1)
	public void test01_unsuccsesfull_connection() {

		String status = "";
		Object object = SQLhandler.connectToDatabase(url, user, "wrong-password");
		if (object instanceof String) {
			String message = (String) object;
			status = message;
		}
		Assert.assertTrue(status.contains("Access denied for user 'root'"));
	};

	@Test(priority = 2)
	public void test02_get_names_of_table_data_columns() throws SQLException {

		String query = "select * from users";
		List<String> metadata = Sqlmethods.getMetaData(user, password, url, query, "names_of_columns");

		Assert.assertEquals(metadata, Arrays.asList("id", "name", "username", "email", "city"));
	};

	@Test(priority = 3)
	public void test03_get_types_of_table_data_columns() throws SQLException {

		String query = "select * from users";
		List<String> metadata = Sqlmethods.getMetaData(user, password, url, query, "types");

		Assert.assertEquals(metadata, Arrays.asList("INT", "VARCHAR", "VARCHAR", "VARCHAR", "VARCHAR"));
	};

	@Test(priority = 4)
	public void test04_check_for_every_element_to_be_notNull() throws SQLException {

		String query = "select * from users";
		ResultSet rs = (ResultSet) Sqlmethods.executeQuerie(user, password, url, query);

		List<List<Object>> allData = MyUtil.getResultSetAsList(rs);

		Assert.assertTrue(MyUtil.checkNotNull(allData));
		Assert.assertTrue(allData.size() > 5);
	};

	@Test(priority = 5)
	public void test05_check_for_every_ID_to_be_in_Asc_order() throws SQLException {

		int index = 1;
		String query = "select * from users";
		ResultSet rs = (ResultSet) Sqlmethods.executeQuerie(user, password, url, query);
		while (rs.next()) {
			Assert.assertTrue(rs.getInt(1) == index);
			index++;
		}
	}

	@Test(priority = 6, enabled = false)
	public void test06_check_for_every_email_to_contains_monkey() throws SQLException {

		String query = "select * from users";
		ResultSet rs = (ResultSet) Sqlmethods.executeQuerie(user, password, url, query);
		while (rs.next()) {
			Assert.assertTrue(rs.getString("email").contains("@"));
		}
	};

	@Test(priority = 7)
	public void test07_check_for_every_userName_to_be_unique() throws SQLException {

		List<String> userNames = new ArrayList<>();
		String query = "select * from users";
		ResultSet rs = (ResultSet) Sqlmethods.executeQuerie(user, password, url, query);
		while (rs.next()) {
			userNames.add(rs.getString("username"));
		}

		Assert.assertTrue(MyUtil.hasNoDuplicates(userNames));
	};

	@Test(priority = 8)
	public void test08_add_items_into_dataBase() throws SQLException {

		int beforeAdding = 0;
		String query01 = "select * from users";
		ResultSet rs01 = (ResultSet) Sqlmethods.executeQuerie(user, password, url, query01);
		while (rs01.next()) {
			beforeAdding++;
		}

		String query = "INSERT INTO users (name, username, email, city) VALUES (?, ?, ?, ?)";
		int targetNumber = 5;

		List<List<String>> list = MyUtil.createUsers(targetNumber);
		int[] newRows = (int[]) Sqlmethods.addIntoDatabase(user, password, url, query, list);

		int afterrAdding = 0;
		ResultSet rs02 = (ResultSet) Sqlmethods.executeQuerie(user, password, url, query01);
		while (rs02.next()) {
			afterrAdding++;
		}

		Assert.assertTrue(newRows.length == targetNumber + 1);
		Assert.assertTrue(newRows.length == afterrAdding - beforeAdding);
	};

	@Test(priority = 9)
	public void test09_check_to_be_unable_to_add_duplicate_usernames() throws SQLException {

		String query = "INSERT INTO users (name, username, email, city) VALUES (?, ?, ?, ?)";
		List<List<String>> list = Arrays.asList(Arrays.asList("pera", "peca", "pp22@gmail.com", "nis"));
		Object message = Sqlmethods.addIntoDatabase(user, password, url, query, list);

		Assert.assertTrue(message.toString().contains("Duplicate entry"));
	}

	@Test(priority = 10)
	public void test10_incorrect_table_name_exception_check() throws SQLException {

		String query = "select * from _wrong_users";
		Object message = Sqlmethods.executeQuerie2(user, password, url, query);

		Assert.assertTrue(message.toString().contains("doesn't exist"));
	}

	@Test(priority = 11)
	public void test11_update_table() throws SQLException {

		String updateQuery = "UPDATE users SET username = ? WHERE id = ?";
		int targetID = 0;
		Object sqlResponse = null;
		List<String> data = new ArrayList<>();
		String newUsername = MyUtil.randomUsername();

		String query = "select * from users";
		ResultSet rs = (ResultSet) Sqlmethods.executeQuerie(user, password, url, query);
		while (rs.next()) {
			data.add(rs.getString("username"));
		}
		targetID = new Random().nextInt(data.size());

		if (!data.stream().anyMatch(u -> u.equals(newUsername))) {
			sqlResponse = Sqlmethods.updateDatabase(user, password, url, updateQuery, newUsername, targetID);
		}
		;
		ResultSet rs1 = (ResultSet) Sqlmethods.executeQuerie(user, password, url, query);
		while (rs1.next()) {
			data.add(rs1.getString("username"));
		}

		Assert.assertEquals(newUsername, MyUtil.findUnique(data).get());
		Assert.assertEquals(sqlResponse, 1);
	}

}
