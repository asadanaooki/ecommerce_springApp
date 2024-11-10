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

/**
 * Mapperテストを行う際に共通のセットアップを行う
 */
public class MapperTestBase {

	/**
	 * ドライバ
	 */
	static final String DRIVER = org.h2.Driver.class.getName();

	/**
	 * URL
	 */
	static final String URL = "jdbc:h2:mem:testdb";

	/**
	 * ユーザー名
	 */
	static final String USER = "sa";

	/**
	 * パスワード
	 */
	static final String PASSWORD = "";

	/**
	 * DatabaseTesterインスタンス
	 */
	static IDatabaseTester databaseTester;

	/**
	 * テストデータのファイルパス
	 */
	final String testDataFilePath;

	/**
	 * コンストラクタ
	 * 
	 * @param testDataFilePath テストデータのファイルパス
	 */
	protected MapperTestBase(String testDataFilePath) {
		this.testDataFilePath = testDataFilePath;
	}

	/**
	 * 全テスト共通のDB接続設定を初期化します。
	 * 
	 * @throws Exception 初期化時の例外
	 */
	@BeforeAll
	static void setUpDbConnection() throws Exception {
		databaseTester = new CustomJdbcDatabaseTester(DRIVER, URL, USER, PASSWORD);
	}

	/**
	 * 各テストの実行前にテストデータをロードします。
	 * 
	 * @throws Exception テストデータのロード中の例外
	 */
	@BeforeEach
	void loadTestData() throws Exception {
		try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(testDataFilePath)) {
			IDataSet dataSet = new FlatXmlDataSetBuilder().build(inputStream);
			databaseTester.setDataSet(dataSet);
			databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
			databaseTester.onSetup();
		}
	}

	/**
	 * 全テスト終了後にデータベースをクリーンアップします。
	 * 
	 * @throws Exception クリーンアップ時の例外
	 */
	@AfterAll
	static void tearDown() throws Exception {
		databaseTester.setTearDownOperation(DatabaseOperation.DELETE_ALL);
		databaseTester.onTearDown();
	}

	/**
	 * カスタムDatabaseTesterクラス
	 */
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
