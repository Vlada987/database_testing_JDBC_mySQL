package tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

import extentReports.Setup;
import sqlOperations.BaseClass;
import sqlOperations.SqlMethods;

public class BaseTest extends Setup {

	BaseClass SQLhandler = new BaseClass();
	SqlMethods Sqlmethods = new SqlMethods();

	public String url = "jdbc:mysql://127.*****06/****";
	public String user = "****";
	public String password = "***********";

	@AfterTest
	public void shutDown() throws SQLException {

		SQLhandler.closeConnection();
	}

};
