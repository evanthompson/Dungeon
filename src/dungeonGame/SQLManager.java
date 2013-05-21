package dungeonGame;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLManager {
	
	private final String PATH = "C:/Users/Evan/workspace/Dungeon/src/dungeonGame/";
	private static int nextInt;
	
	private Connection connection;
	
	public SQLManager() {
		// load the sqlite-JDBC driver using the current class loader
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			System.out.println("ClassNotFoundException");
		}
		
		nextInt = 1;
		connection = null;
		
		createTable("Heros");
	}
	
	public void createTable(String tableName) {
		try {
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:" + PATH + "saves.db");
			DatabaseMetaData meta = connection.getMetaData();
			ResultSet rs = meta.getTables(null, null, tableName, null);
			
			if(!rs.next()) {
				// table tableName does not exist
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(30);  // set timeout to 30 sec.
				
				//statement.executeUpdate("DROP TABLE IF EXISTS " + tableName);
				statement.executeUpdate("CREATE TABLE " + tableName +
						" (id integer, name string, exp integer, money integer)");
			} else {
				System.out.println("table " + tableName + " exists");
			}
		}
		catch(SQLException e) {
			// "out of memory" error --> database file not found
			System.err.println(e.getMessage());
		}
		finally {
			closeConnection();
		}
	}
	
	public void insertRow(String table, String name, int exp, int money) {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + PATH + "saves.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			statement.executeUpdate("INSERT INTO " + table + " values(" + nextInt++ +
					", '" + name + "', " + exp + ", " + money + ")");
		}
		catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		finally {
			closeConnection();
		}
	}
	
	public void updateTable(String table, int rowId, String newName, int newExp, int newMoney) {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + PATH + "saves.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			String update = "UPDATE " + table + 
							" SET name='" + newName + "', exp=" + newExp + ", money=" + newMoney +
							" WHERE id=" + rowId + ";";
			statement.executeUpdate(update);
		}
		catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		finally {
			closeConnection();
		}
	}
	
	public void deleteRow(String table, int rowId) {
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + PATH + "saves.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			String update = "DELETE FROM " + table + " " +
							"WHERE id=" + rowId + ";";
			statement.executeUpdate(update);
		}
		catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		finally {
			closeConnection();
		}
	}
	
	public ArrayList< ArrayList<Object> > getTableRows(String table) {
		ArrayList< ArrayList<Object> > heroList = new ArrayList< ArrayList<Object> >();
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + PATH + "saves.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT * FROM " + table);
			while(rs.next()) {
				ArrayList<Object> statList = new ArrayList<Object>();
				statList.add(rs.getString("name"));
				statList.add(rs.getInt("exp"));
				statList.add(rs.getInt("money"));
				heroList.add(statList);
			}
		}
		catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		finally {
			closeConnection();
		}
		return heroList;
	}
	
	public void printTable(String table) {
		System.out.println("Results from table: " + table + "...");
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + PATH + "saves.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = statement.executeQuery("SELECT * FROM " + table);
			while(rs.next()) {
				System.out.println("id:" + rs.getInt("id") + ", name:" + rs.getString("name") +
						", exp:" + rs.getString("exp") + ", money:" + rs.getInt("money") + ".");
			}
		}
		catch(SQLException e) {
			System.err.println(e.getMessage());
		}
		finally {
			closeConnection();
		}
	}
	
	private void closeConnection() {
		try {
			if(connection != null)
				connection.close();
		}
		catch(SQLException e) {
			System.err.println(e + "failure to close");
		}
	}
	
}
