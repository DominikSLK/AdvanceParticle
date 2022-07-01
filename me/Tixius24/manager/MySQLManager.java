package me.Tixius24.manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.Bukkit;

import me.Tixius24.AdvanceParticle;

public class MySQLManager {

	private AdvanceParticle plugin;
	private String host, database, username, password, table;
	private int port;
	private String driver;
	private final String DEFAULT = "";

	private Connection connection;

	public MySQLManager(AdvanceParticle pl, String host, String database, String username, String password, String table, int port) {
		plugin = pl;
		driver = "com.mysql.jdbc.Driver";

		this.host = host;
		this.database = database;
		this.username = username;
		this.password = password;
		this.table = table;
		this.port = port;

		if (plugin.useMySQL) openConnection();
	}

	public Connection getConnection() {
		return connection;
	}

	public Connection openConnection() {
		try { 

			if (getConnection() != null  && !getConnection().isClosed()) {
				return null;
			}

			Class.forName(driver); 
			connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database , username, password); 

			databaseTable();

			return connection; 
		} catch (SQLException e) { 
			plugin.consoleLog("§c[AP] Storage could not connect to MySQL because: " + e.getMessage());

		} catch (ClassNotFoundException e) {
			plugin.consoleLog("§c[AP] " + driver + " not found!");

		} catch (Exception e) { 
			System.out.println(e.getMessage()); 

		} 

		return connection; 
	}

	public boolean existValue(String identificator, String args) {
		String ID = "Spawner";

		if (identificator.equals("players")) 
			ID = "Player";

		try {
			ResultSet result = query("SELECT * FROM " + table + identificator + " WHERE " + ID +"='" + args + "'", true).getResultSet();

			if (result.next()) {
				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException ex) {}

		return false;
	}

	public void addPlayerData(String playerName, String particle, String date) {
		try {
			query("INSERT IGNORE INTO " + table + "players" + " (Player, Particle, Date) VALUES " 
					+ "('" + playerName + "','" + particle + "','" + date + "');", true);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void updatePlayerData(String playerName, String particle, String date) {
		query("UPDATE " + table + "players" + " SET Particle='" + particle +  " SET Date='" + date +"' WHERE Player='" + playerName + "';", true);
	}

	public void deletePlayerData(String playerName) {
		query("DELETE FROM " + table + "players" + " WHERE Player='" + playerName + "'", true);
	}


	public void saveBlockData(String spawner, String particle, double x, double y, double z, String world, String date) {
		try {	    
			query("INSERT IGNORE INTO " + table + "blocks" + " (ID, Spawner, Particle, X, Y, Z, World, Status, Date) VALUES " 
					+ "('" + getLastTableID() + "','" + spawner + "','" + particle + "','" + x + "','" + y + "','" + z + "','" + world + "','" + "Active" + "','" + date + "');", true);

		} catch (NullPointerException e) {
			e.printStackTrace();
		}
	}

	public void deleteBlockData(String spawnerName) {
		query("UPDATE " + table + "blocks" + " SET Status='" + "Deleted" + "' WHERE Spawner='" + spawnerName + "';", true);
	}

	public String getPlayerData(String player) {
		try {
			ResultSet result = query("SELECT * FROM " + table + "players" + " WHERE Player='" + player + "'", true).getResultSet();

			String particle = DEFAULT;

			while (result.next())
				particle = result.getString("Particle");

			return particle;
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException ex) {}

		return DEFAULT;
	}

	public void getBlockData(int id) {
		try {
			ResultSet result = query("SELECT * FROM " + table + "blocks" + " WHERE ID='" + id + "'", true).getResultSet();

			while (result.next()) {		
				if (result.getString("Status").equals("Active")) {
					plugin.getStream().getBlockStream().put(result.getString("Spawner"), plugin.getAdvanceManager().createObject(result.getString("World"), 
							result.getString("Particle"), result.getDouble("X"), result.getDouble("Y"), result.getDouble("Z")));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (NullPointerException ex) {}

	}

	public int getLastTableID() {
		try {
			ResultSet set = query("SELECT COUNT(*) FROM " + table + "blocks", true).getResultSet();
			set.next();

			int lenght = set.getInt(1);

			return (lenght + 1);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public class Result {
		private ResultSet resultSet;
		private Statement statement;

		public Result(Statement statements, ResultSet resultSets) {
			statement = statements;
			resultSet = resultSets;
		}

		public ResultSet getResultSet() {
			return this.resultSet;
		}

		public void close() {
			try {
				statement.close();
				resultSet.close();

			} catch (SQLException e) {}
		}
	} 

	public boolean isConnected() {
		try {
			return((getConnection()==null || getConnection().isClosed()) ? false:true);

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private Result query(String query, boolean retry) {
		if (!isConnected()) openConnection();

		try {
			PreparedStatement statement=null;

			try {
				statement = getConnection().prepareStatement(query);
				if (statement.execute())
					return new Result(statement, statement.getResultSet());

			} catch (SQLException e) {
				if (retry) {
					plugin.consoleLog("§c[AP] Retrying query...");

					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

						public void run() {
							query(query,false);
						}

					}, 20);

					plugin.consoleLog("§a[AP] Please retry your user action again");
				}
			}

			if (statement != null) statement.close();

		} catch (SQLException ex) {
			System.out.println(ex.getMessage());
		}

		return null;
	}

	private void databaseTable() {
		plugin.consoleLog("§a[AP] Succesfully loading storage provider... MySQL");

		query("CREATE TABLE IF NOT EXISTS " + table + "players" + " (Player varchar(16) NOT NULL, " 
				+ "Particle varchar(32) NOT NULL, Date varchar(32) NOT NULL," + "UNIQUE KEY Player (Player) ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;", true);

		query("CREATE TABLE IF NOT EXISTS " + table + "blocks" + " (ID int(8) NOT NULL,Spawner varchar(32) NOT NULL, Particle varchar(32) NOT NULL, "
				+ "X varchar(16) NOT NULL, " + "Y varchar(16) NOT NULL, Z varchar(16) NOT NULL, World varchar(32) NOT NULL, Status varchar(16) NOT NULL, "
				+ "Date varchar(32) NOT NULL," + "UNIQUE KEY ID (ID) ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;", true);

		plugin.consoleLog("§a[AP] Succesfully loading MySQL database table!");
	}

	public void closeConnection() {
		try {

			if (!getConnection().isClosed()) {
				getConnection().close();
				plugin.consoleLog("§a[AP] Succesfully closing storage provider... MySQL");
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}

}