package main;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

public class DBHandler {
	private String address;
	private int N;
	private Connection conn = null;
	private static final Logger Log = Logger.getLogger(DBHandler.class.getName());
	private static final String PATH_TO_PROPERTIES = "src/main/resources/config.properties";
	private static Properties prop;
	private String sqliteClassName;
	private String deleteQuery;
	private String insertQuery;
	private String selectQuery;
	private String selectQueryField;
	private String connectionType;

	DBHandler() {
		this.address = "";
		this.N = 0;
		try {
			Log.addHandler(new StreamHandler(new FileOutputStream("Application.log", true), new SimpleFormatter()));

		} catch (SecurityException e) {
			Log.log(Level.SEVERE, "Can't create log file. Security error.", e);
		} catch (IOException e) {
			Log.log(Level.SEVERE, "Can't create log file. Input-output error.", e);
		}

		prop = new Properties();

		try {
			FileInputStream stream = new FileInputStream(PATH_TO_PROPERTIES);
			prop.load(stream);
			sqliteClassName = prop.getProperty("sqliteClassName");
			deleteQuery = prop.getProperty("deleteQuery");
			insertQuery = prop.getProperty("insertQuery");
			selectQuery = prop.getProperty("selectQuery");
			selectQueryField = prop.getProperty("selectQueryField");
			connectionType = prop.getProperty("connectionType");
			Log.log(Level.INFO, "Successfully read out properties");
		} catch (IOException e) {
			Log.log(Level.SEVERE, "File " + PATH_TO_PROPERTIES + " not found!", e);
		}

	}

	public String getAdress() {
		return address;
	}

	public void setAdress(String address) {
		this.address = address;
	}

	public int getN() {
		return N;
	}

	public void setN(int n) {
		N = n;
	}

	private void connectToDB() {
		try {
			Class.forName(sqliteClassName);
			conn = DriverManager.getConnection(connectionType + address);
			Log.log(Level.INFO, "Connected to Database");
		} catch (ClassNotFoundException e) {
			Log.log(Level.SEVERE, "Class " + sqliteClassName + " not found!", e);
		} catch (SQLException ex) {
			Log.log(Level.SEVERE, "Error connecting to Database", ex);
		}

	}

	public void deleteFromDB() {
		try {
			if (conn == null)
				this.connectToDB();
			Statement statement;
			statement = conn.createStatement();
			statement.addBatch(deleteQuery);
			statement.executeBatch();

			Log.log(Level.INFO, "Deleted data from 'TEST'");
		} catch (SQLException e) {
			Log.log(Level.SEVERE, "Can't delete data from Database!", e);
		}
	}

	public void writeInDB() {
		try {
			if (conn == null)
				this.connectToDB();
			conn.setAutoCommit(false);
			PreparedStatement statement;
			statement = conn.prepareStatement(insertQuery);
			for (int i = 1; i <= N; i++) {
				statement.setInt(1, i);
				statement.addBatch();
			}
			statement.executeBatch();
			conn.commit();
			Log.log(Level.INFO, "Data successfully written");
		} catch (SQLException e) {
			Log.log(Level.SEVERE, "Can't write data to Database!", e);
		}
	}

	public List<Integer> readFromDB() {
		if (conn == null)
			this.connectToDB();
		ResultSet resSet;

		List<Integer> result = new ArrayList<Integer>();
		try {
			Statement statement = conn.createStatement();
			resSet = statement.executeQuery(selectQuery);
			while (resSet.next()) {
				result.add(resSet.getInt(selectQueryField));
			}
			Log.log(Level.INFO, "Data successfully read out");
		} catch (SQLException e) {
			Log.log(Level.SEVERE, "Can't read data from DataBase!", e);
		}

		return result;
	}

	public void closeDB() {
		try {
			conn.close();
		} catch (SQLException e) {
			Log.log(Level.SEVERE, "Can't close connection to Database", e);
		}
	}
}
