package ec.com.todocompu.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Conexion {

	private String db;
	private String driver;
	private String server;
	private String port;
	private String database;
	private String user;
	private String password;

	private static Conexion conexion;
	private static Connection connection;

	public static void iniciarConeccion(String db, String driver,
			String server, String port, String database, String user,
			String password) {

		Properties props = new Properties();

		if (conexion == null) {
			try {
				conexion = new Conexion(db, driver, server, port, database,
						user, password);

				String url = "jdbc:" + db + "://" + server + ":" + port + "/"
						+ database;

				Class.forName(driver);

				props.setProperty("user", user);
				props.setProperty("password", password);

				connection = DriverManager.getConnection(url, props);

			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static Conexion getConexion() {
		return conexion;
	}

	public static Connection getConnection() {
		return connection;
	}

	private Conexion(String db, String driver, String server, String port,
			String database, String user, String password) {
		this.db = db;
		this.driver = driver;
		this.server = server;
		this.port = port;
		this.database = database;
		this.user = user;
		this.password = password;
	}

	public String getDb() {
		return db;
	}

	public void setDb(String db) {
		this.db = db;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public String getServer() {
		return server;
	}

	public void setServer(String server) {
		this.server = server;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
