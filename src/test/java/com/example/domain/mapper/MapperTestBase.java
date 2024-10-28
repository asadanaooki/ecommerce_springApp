package com.example.domain.mapper;

import java.io.InputStream;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.database.DatabaseConfig;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class MapperTestBase {
	protected static final String DRIVER = org.h2.Driver.class.getName();
	protected static final String URL = "jdbc:h2:mem:testdb";
	protected static final String USER = "sa";
	protected static final String PASSWORD = "";
	protected static IDatabaseTester databaseTester;
	protected final String testDataFilePath;

	protected MapperTestBase(String testDataFilePath) {
		this.testDataFilePath = testDataFilePath;
	}

	@BeforeAll
	static void setUpDbConnection() throws Exception {
		databaseTester = new CustomJdbcDatabaseTester(DRIVER, URL, USER, PASSWORD);
	}

	@BeforeEach
	void loadTestData() throws Exception {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(testDataFilePath)) {
			IDataSet dataSet = new FlatXmlDataSetBuilder().build(inputStream);
			databaseTester.setDataSet(dataSet);
			databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
			databaseTester.onSetup();
		}
	}

	@AfterAll
	static void tearDown() throws Exception {
		databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
		databaseTester.onTearDown();
	}

	public static class CustomJdbcDatabaseTester extends JdbcDatabaseTester {
		public CustomJdbcDatabaseTester(String driverClass, String connectionUrl, String username, String password)
				throws Exception {
			super(driverClass, connectionUrl, username, password);
		}
		
		@Override
		public IDatabaseConnection getConnection() throws Exception {
			IDatabaseConnection connection = super.getConnection();
			
			connection.getConfig().setProperty(DatabaseConfig.PROPERTY_ESCAPE_PATTERN, "\"?\"");
			
			return connection;
		}
	}
}
